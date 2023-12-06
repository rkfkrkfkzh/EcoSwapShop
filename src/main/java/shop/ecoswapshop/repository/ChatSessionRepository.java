package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ecoswapshop.domain.ChatSession;

import java.util.List;
import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {

    // 적절한 쿼리 메소드를 여기에 추가할 수 있습니다. 예를 들어:
    ChatSession findByProductIdAndSellerIdAndBuyerId(Long productId, String sellerId, String buyerId);

}
