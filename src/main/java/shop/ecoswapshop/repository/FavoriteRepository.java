package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Favorite;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 사용자 ID와 물건 ID로 Dibs를 조회하는 메서드 추가
    Favorite findByMemberIdAndProductId(Long memberId, Long productId);

    // 특정 사용자가 작성한 모든찜 조회
    List<Favorite> findByMemberId(Long memberId);

    List<Favorite> findByProductId(Long productId);

}
