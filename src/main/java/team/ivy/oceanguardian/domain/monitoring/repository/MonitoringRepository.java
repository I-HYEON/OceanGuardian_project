package team.ivy.oceanguardian.domain.monitoring.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT m FROM Monitoring m WHERE m.id = :monitoringId")
    Monitoring findMonitoringWithMember(@Param("monitoringId") Long monitoringId);

    Page<Monitoring> findAllByMember (Member member, Pageable pageable);

    List<Monitoring> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    Page<Monitoring> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    @Query(value = "SELECT DISTINCT ON (m.coast_name) m.coast_name " +
        "FROM monitoring m WHERE m.coast_name LIKE :keyword% ", nativeQuery = true)
    List<String> findCoastNamesByKeyword(@Param("keyword") String keyword);
}
