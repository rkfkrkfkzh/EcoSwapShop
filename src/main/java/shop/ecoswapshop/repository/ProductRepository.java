package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Product;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상품 이름으로 조회
    List<Product> findByProductName(String productName);

    // 판매자 이름으로 조회
    List<Product> findByMemberFullName(String fullName);

    // 상품 가격으로 조회 (특정 가격 이하의 상품들)
    List<Product> findByPriceLessThan(int price);

    // 상품 등록일 기준으로 조회 (특정 날짜 이후의 상품들)
    List<Product> findByCreationDateAfter(LocalDateTime creationDate);

    // 상품 등록일과 판매자 이름으로 복합 조건으로 조회
    List<Product> findByCreationDateAfterAndMemberFullName(LocalDateTime creationDate, String fullName);

    // 상품 삭제
    void deleteById(Long id);

    // 판매자 이름과 상품 이름으로 복합 조건으로 조회
    List<Product> findByMemberFullNameAndProductName(String fullName, String productName);

}
