package team.ivy.oceanguardian.domain.monitoring.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
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
        String serialNumber = generateSerialNumber();

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

//     일련번호 생성 메서드
    private String generateSerialNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        String datePrefix = sdf.format(new Date());
        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        return datePrefix + uuidPart;
    }

    public MonitoringResponse getMonitoring(Long monitoringId) {

        Monitoring monitoring = monitoringRepository.findById(monitoringId).orElseThrow();
        List<Image> images = imageRepository.findByMonitoring(monitoring);

        return MonitoringResponse.toDto(monitoring, images.get(0).getUrl());
    }

    @Transactional
    public Void deleteMonitoring(Long monitoringId) {

        Monitoring monitoring = monitoringRepository.findById(monitoringId).orElseThrow();
        imageRepository.deleteByMonitoring(monitoring);
        monitoringRepository.delete(monitoring);

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
    public List<MonitoringResponse> getMonitoringsBetween(LocalDateTime startTime, LocalDateTime endTime) {

        List<Monitoring> monitorings = monitoringRepository.findAllByCreatedAtBetween(startTime, endTime);
        log.info("getMonitoringsBetween 데이터 개수"+monitorings.size());
        return monitorings.stream().map(MonitoringResponse::toDto).toList();
    }
}
