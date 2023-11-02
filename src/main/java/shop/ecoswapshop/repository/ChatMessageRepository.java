package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.ecoswapshop.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 현재 사용자 ID에 해당하는 채팅 메시지를 조회
    List<ChatMessage> findByReceiverId(String currentUserId);

    List<ChatMessage> findByReceiverIdAndSenderIdOrReceiverIdAndSenderId(String receiverId, String name, String name1, String receiverId1);

    List<ChatMessage> findByProduct_Id(Long productId);

    List<ChatMessage> findByProduct_IdAndSenderIdAndReceiverId(Long productId, String senderId, String receiverId);

    List<ChatMessage> findByProduct_IdAndReceiverId(Long productId, String sellerId);


}
