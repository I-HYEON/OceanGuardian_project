package team.ivy.oceanguardian.domain.cleanup.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupRequest;
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

//    @Transactional
//    public Long saveCleanup(CleanupRequest cleanupRequest) throws IOException {
//        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member member = memberRepository.findByPhoneNumber(username)
//            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
//
//        // 위도와 경도를 PostGIS Point 객체로 변환
//        Point location = geometryFactory.createPoint(new Coordinate(cleanupRequest.getLongitude(), cleanupRequest.getLatitude()));
//
//        // 일련번호 생성 (yyyyMMddHH + UUID 7자리)
//        String serialNumber = generateSerialNumber();
//
//        // 청소 데이터 저장
//        Cleanup savedCleanup = cleanupRepository.save(cleanupRequest.toEntity(serialNumber, location, member));
//
//        // S3에 청소전 이미지 파일 업로드 후, Image 엔티티 저장
//        MultipartFile BeforeCleanupViewFile = cleanupRequest.getBeforeCleanupView();
//        if (BeforeCleanupViewFile != null && !BeforeCleanupViewFile.isEmpty()) {
//            String imageUrl = s3Service.uploadFile(BeforeCleanupViewFile,"청소전", serialNumber);
//
//            Image image = Image.builder()
//                .url(imageUrl)
//                .description("청소전")
//                .cleanup(savedCleanup)
//                .build();
//
//            imageRepository.save(image);
//        }
//
//        // S3에 청소전 이미지 파일 업로드 후, Image 엔티티 저장
//        MultipartFile AfterCleanupViewFile = cleanupRequest.getAfterCleanupView();
//        if (AfterCleanupViewFile != null && !AfterCleanupViewFile.isEmpty()) {
//            String imageUrl = s3Service.uploadFile(AfterCleanupViewFile,"청소후", serialNumber);
//
//            Image image = Image.builder()
//                .url(imageUrl)
//                .description("청소후")
//                .cleanup(savedCleanup)
//                .build();
//
//            imageRepository.save(image);
//        }
//
//        // S3에 청소전 이미지 파일 업로드 후, Image 엔티티 저장
//        MultipartFile CleanupCompleteViewFile = cleanupRequest.getCleanupCompleteView();
//        if (CleanupCompleteViewFile != null && !CleanupCompleteViewFile.isEmpty()) {
//            String imageUrl = s3Service.uploadFile(CleanupCompleteViewFile,"집하완료", serialNumber);
//
//            Image image = Image.builder()
//                .url(imageUrl)
//                .description("집하완료")
//                .cleanup(savedCleanup)
//                .build();
//
//            imageRepository.save(image);
//        }
//
//        return savedCleanup.getId();
//    }
//
//    // 일련번호 생성 메서드
//    private String generateSerialNumber() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//        String datePrefix = sdf.format(new Date());
//        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
//        return datePrefix + uuidPart;
//    }
}
