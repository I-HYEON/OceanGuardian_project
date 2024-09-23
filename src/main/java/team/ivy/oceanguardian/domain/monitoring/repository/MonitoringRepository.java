package team.ivy.oceanguardian.domain.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {

}
