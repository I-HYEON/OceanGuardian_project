package team.ivy.oceanguardian.domain.monitoring.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.admin.service.ExcelService;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.image.repository.ImageRepository;
import team.ivy.oceanguardian.domain.image.service.S3Service;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.member.repository.MemberRepository;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringListResponse;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringRequest;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringSummary;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;
import team.ivy.oceanguardian.domain.monitoring.repository.MonitoringRepository;
import team.ivy.oceanguardian.global.exception.CustomException;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public Long createMonitoring(MonitoringRequest monitoringRequest, MultipartFile monitoringViewFile) throws IOException {

        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 위도와 경도를 PostGIS Point 객체로 변환
        Point location = geometryFactory.createPoint(new Coordinate(monitoringRequest.getLongitude(), monitoringRequest.getLatitude()));

        // 일련 번호 생성 (yyyyMMddHH + UUID 7자리)
        String serialNumber = generateSerialNumber(monitoringRequest.getMainTrashType());

        // 조사 데이터 저장
        Monitoring savedMonitoring = monitoringRepository.save(monitoringRequest.toEntity(serialNumber, location, member));

        // S3에 이미지 파일 업로드 후, Image 엔티티 저장
        if (monitoringViewFile != null && !monitoringViewFile.isEmpty()) {

            String imageUrl = s3Service.uploadFile(monitoringViewFile,"조사완료", serialNumber);

            Image image = Image.builder()
                .url(imageUrl)
                .description("조사완료")
                .monitoring(savedMonitoring)
                .build();

            imageRepository.save(image);
        }

        return savedMonitoring.getId();
    }

    // 일련 번호 생성 메서드
    private String generateSerialNumber(byte mainTrashType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datePrefix = sdf.format(new Date());
        // String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        String typePart = mainTrashType + "00";
        return datePrefix + typePart;
    }

    public MonitoringResponse getMonitoring(Long monitoringId) {

        Monitoring monitoring = monitoringRepository.findMonitoringWithMember(monitoringId);
        List<Image> images = imageRepository.findByMonitoring(monitoring);

        // 이미지 리스트가 비어 있는지 확인
        String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

        return MonitoringResponse.toDto(monitoring, imageUrl);
    }

    @Transactional
    public Void deleteMonitoring(Long monitoringId) {

        Monitoring monitoring = monitoringRepository.findById(monitoringId).orElseThrow();
        List<Image> images = imageRepository.findByMonitoring(monitoring);
        try {
            for (Image image: images) {
                s3Service.deleteFile(image.getDescription()+monitoring.getSerialNumber()+".webp");
            }
            imageRepository.deleteByMonitoring(monitoring);
            monitoringRepository.delete(monitoring);
        } catch (Exception e) {
            log.error("S3 파일 삭제 또는 DB 작업 중 오류 발생: {}", monitoringId, e);
            throw new CustomException(ErrorCode.IMAGE_DELETE_ERROR);
        }

        return null;
    }

    @Transactional
    public MonitoringListResponse getMonitoringList(Pageable pageable) {
        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Page<Monitoring> monitoringPage = monitoringRepository.findAllByMember(member, pageable);
        Page<MonitoringSummary> monitoringResponsePage = monitoringPage.map(
            MonitoringSummary::toDto);

        return MonitoringListResponse.builder()
            .maxPage(monitoringPage.getTotalPages())
            .nowPage(monitoringPage.getNumber())
            .totalCount(monitoringPage.getTotalElements())
            .monitoringList(monitoringResponsePage.getContent())
            .build();
    }

    @Transactional
    public List<String> getAutocompleteResults(String keyword) {
        List<String> coastNames = monitoringRepository.findCoastNamesByKeyword(keyword);
        return coastNames.stream()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
    }

    @Transactional
    public MonitoringListResponse getMonitoringListLatest(Pageable pageable) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMonths(3);

        Page<Monitoring> monitoringPage = monitoringRepository.findAllByCreatedAtBetween(startTime,endTime,pageable);
        Page<MonitoringSummary> monitoringResponsePage = monitoringPage.map(
            MonitoringSummary::toDto);

        return MonitoringListResponse.builder()
            .maxPage(monitoringPage.getTotalPages())
            .nowPage(monitoringPage.getNumber())
            .totalCount(monitoringPage.getTotalElements())
            .monitoringList(monitoringResponsePage.getContent())
            .build();
    }
}
