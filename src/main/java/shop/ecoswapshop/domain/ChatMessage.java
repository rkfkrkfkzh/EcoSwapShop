package shop.ecoswapshop.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
public class ChatMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    // ChatSession과의 연관 관계 추가
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "session_id") // 외래 키 명시
    @JsonBackReference // 자식 엔티티 측면 (직렬화할 때 무시)
    private ChatSession chatSession;

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            product.getChatMessages().add(this);
        }
    }

    // ChatSession setter 수정
    public void setChatSession(ChatSession chatSession) {
        // 기존 연결을 제거합니다.
        if (this.chatSession != null) {
            this.chatSession.getMessages().remove(this);
        }

        // 새로운 ChatSession을 설정합니다.
        this.chatSession = chatSession;

        // 새로운 ChatSession에 현재 메시지를 추가합니다.
        if (chatSession != null && !chatSession.getMessages().contains(this)) {
            chatSession.getMessages().add(this);
        }
    }
}
