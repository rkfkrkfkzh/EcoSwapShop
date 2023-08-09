package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ecoswapshop.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
