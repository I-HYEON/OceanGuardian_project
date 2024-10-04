package team.ivy.oceanguardian.domain.cleanup.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupListResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupRequest;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupSummary;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupWithDistance;
import team.ivy.oceanguardian.domain.cleanup.dto.CoastAvg;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.cleanup.repository.CleanupRepository;
import team.ivy.oceanguardian.domain.cleanup.utils.PointConverter;
import team.ivy.oceanguardian.domain.excel.service.ExcelService;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.image.repository.ImageRepository;
import team.ivy.oceanguardian.domain.image.service.S3Service;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.member.repository.MemberRepository;
import team.ivy.oceanguardian.global.exception.CustomException;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class CleanupService {
    private final CleanupRepository cleanupRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final ExcelService excelService;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public Long createCleanup(CleanupRequest cleanupRequest, MultipartFile beforeViewFile, MultipartFile afterViewFile, MultipartFile completeViewFile) throws IOException {
        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 위도와 경도를 PostGIS Point 객체로 변환
        Point location = geometryFactory.createPoint(new Coordinate(cleanupRequest.getLongitude(), cleanupRequest.getLatitude()));

        // 일련번호 생성
        String serialNumber = generateSerialNumber(cleanupRequest.getMainTrashType());

        // 청소 데이터 저장
        Cleanup savedCleanup = cleanupRepository.save(cleanupRequest.toEntity(serialNumber, location, member));

        // S3에 청소전 이미지 파일 업로드 후, Image 엔티티 저장
        if (beforeViewFile != null && !beforeViewFile.isEmpty()) {
            String imageUrl = s3Service.uploadFile(beforeViewFile,"청소전", serialNumber);

            Image image = Image.builder()
                .url(imageUrl)
                .description("청소전")
                .cleanup(savedCleanup)
                .build();

            imageRepository.save(image);
        }

        // S3에 청소후 이미지 파일 업로드 후, Image 엔티티 저장
        if (afterViewFile != null && !afterViewFile.isEmpty()) {
            String imageUrl = s3Service.uploadFile(afterViewFile,"청소후", serialNumber);

            Image image = Image.builder()
                .url(imageUrl)
                .description("청소후")
                .cleanup(savedCleanup)
                .build();

            imageRepository.save(image);
        }

        // S3에 집하완료 이미지 파일 업로드 후, Image 엔티티 저장
        if (completeViewFile != null && !completeViewFile.isEmpty()) {
            String imageUrl = s3Service.uploadFile(completeViewFile,"집하완료", serialNumber);

            Image image = Image.builder()
                .url(imageUrl)
                .description("집하완료")
                .cleanup(savedCleanup)
                .build();

            imageRepository.save(image);
        }

        return savedCleanup.getId();
    }

    // 일련 번호 생성 메서드
    private String generateSerialNumber(byte mainTrashType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datePrefix = sdf.format(new Date());
//        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        String typePart = mainTrashType + "00";
        return datePrefix + typePart;
    }

    public CleanupResponse getCleanup(Long cleanupId) {

        Cleanup cleanup = cleanupRepository.findCleanupWithMember(cleanupId);
        List<Image> images = imageRepository.findByCleanup(cleanup);

        String beforeViewImage = null;
        String afterViewImage = null;
        String completeViewImage = null;

        for (Image image:images) {
            String description = image.getDescription();
            String url = image.getUrl();

            if ("청소전".equals(description)) {
                beforeViewImage = url;
            } else if ("청소후".equals(description)) {
                afterViewImage = url;
            } else if ("집하완료".equals(description)) {
                completeViewImage = url;
            }
        }

        return CleanupResponse.toDto(cleanup, beforeViewImage, afterViewImage, completeViewImage);
    }

    @Transactional
    public Void deleteCleanup(Long cleanupId) {

        Cleanup cleanup = cleanupRepository.findById(cleanupId).orElseThrow();
        List<Image> images = imageRepository.findByCleanup(cleanup);

        try {
            for (Image image: images) {
                s3Service.deleteFile(image.getDescription()+cleanup.getSerialNumber()+".webp");
            }
            imageRepository.deleteByCleanup(cleanup);
            cleanupRepository.delete(cleanup);

        } catch (Exception e) {
            log.error("S3 파일 삭제 또는 DB 작업 중 오류 발생: {}", cleanupId, e);
            throw new CustomException(ErrorCode.IMAGE_DELETE_ERROR);
        }

        return null;
    }

    @Transactional
    public CleanupListResponse getCleanupList(Pageable pageable) {

        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Page<Cleanup> cleanupPage = cleanupRepository.findAllByMember(member, pageable);
        Page<CleanupSummary> cleanupResponsePage = cleanupPage.map(
            CleanupSummary::toDto);

        return CleanupListResponse.builder()
            .maxPage(cleanupPage.getTotalPages())
            .nowPage(cleanupPage.getNumber())
            .totalCount(cleanupPage.getTotalElements())
            .cleanupList(cleanupResponsePage.getContent())
            .build();
    }

    @Transactional
    public List<CleanupResponse> getCleanupsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<Cleanup> cleanups = cleanupRepository.findAllByCreatedAtBetween(startTime, endTime);
        log.info("getCleanupsBetween 데이터 개수"+cleanups.size());
        return cleanups.stream().map(CleanupResponse::toDto).toList();
    }

    @Transactional
    public void downloadCleanupData(LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response)
        throws IOException {
        List<Cleanup> cleanups = cleanupRepository.findAllByCreatedAtBetween(startTime, endTime);

        excelService.downloadCleanupExcelFile(cleanups, response);
    }

    @Transactional
    public List<CleanupResponse> getCleanupsNotPickup() {

        List<Cleanup> cleanups = cleanupRepository.findAllByPickupDone(false);
        log.info("getCleanupsNotPickup 데이터 개수"+cleanups.size());
        return cleanups.stream().map(cleanup -> {
            Optional<Image> image = imageRepository.findByCleanupAndDescription(cleanup, "집하완료");
            return CleanupResponse.toDto(cleanup, image);
        }).toList();
    }

    @Transactional
    public Void updatePickupStatusToTrue(Long cleanupId) {
        Cleanup cleanup = cleanupRepository.findById(cleanupId).orElseThrow();

        // 청소 데이터 pickupDone 업데이트 후 저장
        Cleanup savedCleanup = cleanupRepository.save(Cleanup.builder()
            .id(cleanup.getId())
            .serialNumber(cleanup.getSerialNumber())
            .location(cleanup.getLocation())
            .coastName(cleanup.getCoastName())
            .coastLength(cleanup.getCoastLength())
            .actualTrashVolume(cleanup.getActualTrashVolume())
            .mainTrashType(cleanup.getMainTrashType())
            .member(cleanup.getMember())
            .pickupDone(Boolean.TRUE)
            .build());

        return null;
    }

    @Transactional
    public Void updatePickupStatusToFalse(Long cleanupId) {
        Cleanup cleanup = cleanupRepository.findById(cleanupId).orElseThrow();

        // 청소 데이터 pickupDone 업데이트 후 저장
        Cleanup savedCleanup = cleanupRepository.save(Cleanup.builder()
            .id(cleanup.getId())
            .serialNumber(cleanup.getSerialNumber())
            .location(cleanup.getLocation())
            .coastName(cleanup.getCoastName())
            .coastLength(cleanup.getCoastLength())
            .actualTrashVolume(cleanup.getActualTrashVolume())
            .mainTrashType(cleanup.getMainTrashType())
            .member(cleanup.getMember())
            .pickupDone(Boolean.FALSE)
            .build());

        return null;
    }

    @Transactional
    public CleanupWithDistance getClosestCleanup(double latitude, double longitude) {

        /* Point 객체 생성 */
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        /* 좌표계 설정 */
        location.setSRID(4326);

        /* 위치로부터 가장 가까운 객체가 담긴 배열 가져오기 */
        Object[] result = cleanupRepository.findClosestCleanup(location).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_ENTITY));
        /* 원소 꺼내오기 */
        Object[] row = (Object[]) result[0];

        // 배열의 각 요소를 적절한 타입으로 변환
        String coastName = (String) row[0];
        Point cleanupLocation = PointConverter.convertToJTSPoint((org.geolatte.geom.Point) row[1]);
        double location_lat = cleanupLocation.getY();
        double location_lng = cleanupLocation.getX();
        double distance = ((Number) row[2]).doubleValue();

        return CleanupWithDistance.toDto(coastName,location_lat,location_lng,distance);
    }
    @Transactional
    public List<CoastAvg> getGroupedByCoastName() {
        List<Object[]> results = cleanupRepository.findGroupedByCoastName();

        return results.stream()
            .sorted(Comparator.comparing((Object[] row) -> ((Double) row[2] * 50) / (Double) row[1]).reversed())
            .map(row -> CoastAvg.toDto(
            (String) row[0],
            Double.parseDouble(String.format("%.4f", ((Double) row[2] * 50) / ((Double) row[1]))),
            (Double) row[3],
            (Double) row[4]
        )).toList();
    }

    @Transactional
    public void downloadAvgData(HttpServletResponse response)
        throws IOException {
        List<Object[]> results = cleanupRepository.findGroupedByCoastName();

        excelService.downloadAvgExcelFile(results, response);
    }

    @Transactional
    public CleanupListResponse getCleanupListLatest(Pageable pageable) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMonths(3);

        Page<Cleanup> cleanupPage = cleanupRepository.findAllByCreatedAtBetween(startTime, endTime, pageable);
        Page<CleanupSummary> cleanupResponsePage = cleanupPage.map(
            CleanupSummary::toDto);

        return CleanupListResponse.builder()
            .maxPage(cleanupPage.getTotalPages())
            .nowPage(cleanupPage.getNumber())
            .totalCount(cleanupPage.getTotalElements())
            .cleanupList(cleanupResponsePage.getContent())
            .build();
    }

    @Transactional
    public List<String> getAutocompleteResults(String keyword) {
        List<String> coastNames = cleanupRepository.findCoastNamesByKeyword(keyword);
        return coastNames.stream()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
    }
}
