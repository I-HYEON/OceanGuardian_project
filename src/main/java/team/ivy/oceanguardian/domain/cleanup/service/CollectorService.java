package team.ivy.oceanguardian.domain.cleanup.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupWithDistance;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.cleanup.repository.CleanupRepository;
import team.ivy.oceanguardian.domain.cleanup.utils.PointConverter;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.image.repository.ImageRepository;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.member.repository.MemberRepository;
import team.ivy.oceanguardian.global.exception.CustomException;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectorService {

    private final CleanupRepository cleanupRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public List<CleanupResponse> getCleanupsNotPickup() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByPhoneNumber(username)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<Cleanup> cleanups;
        if (member.getRoles().get(0).equals("ADMIN")) {
            log.info("ADMIN입니다");
            cleanups = cleanupRepository.findAllByPickupDone(false);
        } else if (member.getRoles().get(0).equals("USER")) {
            log.info("USER입니다");
            cleanups = cleanupRepository.findByPickupDoneAndWorkerId(false, member.getId());
        } else {
            // 적절하지 않은 역할에 대해 처리 (Optional)
            throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        }

        log.info("getCleanupsNotPickup 데이터 개수"+cleanups.size());
        return cleanups.stream().map(cleanup -> {
            Optional<Image> image = imageRepository.findByCleanupAndDescription(cleanup, "집하완료");
            Optional<Member> worker = cleanup.getWorkerId() != null
                ? memberRepository.findById(cleanup.getWorkerId())
                : Optional.empty();
            return CleanupResponse.toDto(cleanup, image, worker);
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
    public Void assignWorker(List<Long> cleanupIdList, Long memberId) {
        for (Long cleanupId : cleanupIdList) {
            // cleanupId로 Cleanup 객체 조회
            Cleanup cleanup = cleanupRepository.findById(cleanupId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ENTITY));

            cleanupRepository.save(
                Cleanup.builder()
                    .id(cleanup.getId())  // 기존 ID 그대로 유지
                    .serialNumber(cleanup.getSerialNumber())
                    .location(cleanup.getLocation())
                    .coastName(cleanup.getCoastName())
                    .coastLength(cleanup.getCoastLength())
                    .actualTrashVolume(cleanup.getActualTrashVolume())
                    .mainTrashType(cleanup.getMainTrashType())
                    .pickupDone(cleanup.getPickupDone())
                    .workerId(memberId)  // workerId만 새롭게 설정
                    .member(cleanup.getMember())
                    .build()
            );
        }
        return null;
    }
}
