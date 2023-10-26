package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ecoswapshop.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 현재 사용자 ID에 해당하는 채팅 메시지를 조회
    List<ChatMessage> findByReceiverId(String currentUserId);
}
