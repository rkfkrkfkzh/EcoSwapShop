package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ecoswapshop.domain.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByProductId(Long productId);

    // 상품 삭제
    void deleteById(Long id);
}
