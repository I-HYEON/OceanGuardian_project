package team.ivy.oceanguardian.domain.monitoring.service;

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
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.image.repository.ImageRepository;
import team.ivy.oceanguardian.domain.image.service.S3Service;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.member.repository.MemberRepository;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringRequest;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;
import team.ivy.oceanguardian.domain.monitoring.repository.MonitoringRepository;
import team.ivy.oceanguardian.global.exception.CustomException;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public Long saveMonitoring(MonitoringRequest monitoringRequest, MultipartFile imageFile) throws IOException {
        // 현재 로그인된 사용자 정보를 토대로 Member 객체 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 위도와 경도를 PostGIS Point 객체로 변환
        Point location = geometryFactory.createPoint(new Coordinate(monitoringRequest.getLongitude(), monitoringRequest.getLatitude()));

        // 일련번호 생성 (yyyyMMddHH + UUID 7자리)
        String serialNumber = generateSerialNumber();

        // 조사 데이터 저장
        Monitoring savedMonitoring = monitoringRepository.save(monitoringRequest.toEntity(serialNumber, location, member));

        // S3에 청소전 이미지 파일 업로드 후, Image 엔티티 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = s3Service.uploadFile(imageFile,"조사완료", serialNumber);

            Image image = Image.builder()
                .url(imageUrl)
                .description("조사완료")
                .monitoring(savedMonitoring)
                .build();

            imageRepository.save(image);
        }

        return savedMonitoring.getId();
    }

    // 일련번호 생성 메서드
    private String generateSerialNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        String datePrefix = sdf.format(new Date());
        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        return datePrefix + uuidPart;
    }
}
