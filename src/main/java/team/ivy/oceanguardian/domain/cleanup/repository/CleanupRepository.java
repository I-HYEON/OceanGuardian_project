package team.ivy.oceanguardian.domain.cleanup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;

@Repository
public interface CleanupRepository extends JpaRepository<Cleanup, Long> {

}
