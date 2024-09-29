package team.ivy.oceanguardian.domain.cleanup.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.member.entity.Member;

@Repository
public interface CleanupRepository extends JpaRepository<Cleanup, Long> {

    Page<Cleanup> findAllByMember(Member member, Pageable pageable);

    List<Cleanup> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Cleanup> findAllByPickupDone(Boolean pickupDone);

    @Query(value = "SELECT c.coast_name, c.location, ST_DistanceSpheroid(c.location, :location) AS distance " +
        "FROM cleanup c " +
        "ORDER BY distance ASC " +
        "LIMIT 1",
        nativeQuery = true)
    Optional<Object[]> findClosestCleanup(Point location);
}
