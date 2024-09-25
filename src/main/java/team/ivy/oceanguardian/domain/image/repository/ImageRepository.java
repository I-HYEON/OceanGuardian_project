package team.ivy.oceanguardian.domain.image.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByMonitoring(Monitoring monitoring);

    List<Image> findByCleanup(Cleanup cleanup);

    void deleteByMonitoring(Monitoring monitoring);
    void deleteByCleanup(Cleanup cleanup);

}
