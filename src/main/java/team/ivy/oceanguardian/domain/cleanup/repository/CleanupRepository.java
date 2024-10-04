package team.ivy.oceanguardian.domain.cleanup.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.member.entity.Member;

@Repository
public interface CleanupRepository extends JpaRepository<Cleanup, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT c FROM Cleanup c WHERE c.id = :cleanupId")
    Cleanup findCleanupWithMember(@Param("cleanupId") Long cleanupId);

    Page<Cleanup> findAllByMember(Member member, Pageable pageable);

    List<Cleanup> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Cleanup> findAllByPickupDone(Boolean pickupDone);

    Page<Cleanup> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    @Query(value = "SELECT c.coast_name, c.location, ST_DistanceSphere(c.location, :location) AS distance " +
        "FROM cleanup c " +
        "WHERE c.pickup_done = false " +
        "ORDER BY distance ASC " +
        "LIMIT 1",
        nativeQuery = true)
    Optional<Object[]> findClosestCleanup(Point location);

    @Query(value = "SELECT c.coast_name AS coastName, " +
        "AVG(c.coast_length) AS avgCoastLength, " +
        "AVG(c.actual_trash_volume) AS avgTrashVolume, " +
        "MIN(ST_Y(c.location)) AS latitude, " +
        "MIN(ST_X(c.location)) AS longitude " +
        "FROM cleanup c " +
        "GROUP BY c.coast_name ",
        nativeQuery = true)
    List<Object[]> findGroupedByCoastName();

    @Query(value = "SELECT DISTINCT ON (c.coast_name) c.coast_name " +
        "FROM cleanup c WHERE c.coast_name LIKE :keyword% ", nativeQuery = true)
    List<String> findCoastNamesByKeyword(@Param("keyword") String keyword);
}
