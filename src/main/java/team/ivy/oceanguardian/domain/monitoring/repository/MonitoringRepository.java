package team.ivy.oceanguardian.domain.monitoring.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {

    Page<Monitoring> findAllByMember (Member member, Pageable pageable);

    List<Monitoring> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}
