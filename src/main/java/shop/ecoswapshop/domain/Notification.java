package shop.ecoswapshop.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    private String senderId;
    private String receiverId;
    private String content;
    private boolean isRead = false;
    private String productName; // 상품ID 추가
    private Long productId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "session_id") // 외래 키 명시
    private ChatSession chatSession; // ChatSession과의 연관 관계 추가
}
