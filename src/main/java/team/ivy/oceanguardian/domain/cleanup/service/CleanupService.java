package team.ivy.oceanguardian.domain.cleanup.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.cleanup.repository.CleanupRepository;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.image.repository.ImageRepository;
import team.ivy.oceanguardian.domain.image.service.S3Service;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.member.repository.MemberRepository;
import team.ivy.oceanguardian.global.exception.CustomException;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Service
@RequiredArgsConstructor
public class CleanupService {
    private final CleanupRepository cleanupRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public Long createCleanup(CleanupRequest cleanupRequest, MultipartFile beforeViewFile, MultipartFile afterViewFile, MultipartFile completeViewFile) throws IOException {
        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 위도와 경도를 PostGIS Point 객체로 변환
        Point location = geometryFactory.createPoint(new Coordinate(cleanupRequest.getLongitude(), cleanupRequest.getLatitude()));

        // 일련번호 생성 (yyyyMMddHH + UUID 7자리)
        String serialNumber = generateSerialNumber();

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
    private String generateSerialNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        String datePrefix = sdf.format(new Date());
        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        return datePrefix + uuidPart;
    }

    public CleanupResponse getCleanup(Long cleanupId) {

        Cleanup cleanup = cleanupRepository.findById(cleanupId).orElseThrow();
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
        imageRepository.deleteByCleanup(cleanup);
        cleanupRepository.delete(cleanup);

        return null;
    }

    @Transactional
    public CleanupListResponse getCleanupList(Pageable pageable) {
        Page<Cleanup> cleanupPage = cleanupRepository.findAll(pageable);
        Page<CleanupSummary> cleanupResponsePage = cleanupPage.map(
            CleanupSummary::toDto);

        return CleanupListResponse.builder()
            .maxPage(cleanupPage.getTotalPages())
            .nowPage(cleanupPage.getNumber())
            .totalCount(cleanupPage.getTotalElements())
            .cleanupList(cleanupResponsePage.getContent())
            .build();
    }
}
