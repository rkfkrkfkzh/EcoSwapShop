package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Notification;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByReceiverId(String name);

    void deleteByChatSession_SessionId(UUID sessionId);
}
