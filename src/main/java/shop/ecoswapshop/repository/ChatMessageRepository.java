package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ecoswapshop.domain.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByProduct_IdAndSenderIdAndReceiverId(Long productId, String senderId, String receiverId);

    List<ChatMessage> findByProduct_IdAndReceiverId(Long productId, String sellerId);

    // 특정 채팅 세션의 메시지를 조회
    List<ChatMessage> findByChatSession_SessionId(UUID sessionId);

    // 특정 채팅 세션의 모든 메시지를 삭제
    void deleteByChatSession_SessionId(UUID sessionId);
}
