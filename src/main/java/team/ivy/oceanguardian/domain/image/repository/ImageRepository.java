package team.ivy.oceanguardian.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.ivy.oceanguardian.domain.image.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
