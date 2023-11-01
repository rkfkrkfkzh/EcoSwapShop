package shop.ecoswapshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
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
}
