package shop.ecoswapshop.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class ChatSession {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "CHAR(36)") //mysql에서 사용하기위해 변경
    private UUID sessionId;

    private Long productId;
    private String sellerId;
    private String buyerId;

    // ChatMessage와의 양방향 연관 관계 추가
    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 부모엔티티 측면 (직렬화 무한재귀 방지)
    private List<ChatMessage> messages = new ArrayList<>();

    // 메시지 리스트 관리 메소드
    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setChatSession(this);
    }

    public void removeMessage(ChatMessage message) {
        messages.remove(message);
        message.setChatSession(null);
    }
}
