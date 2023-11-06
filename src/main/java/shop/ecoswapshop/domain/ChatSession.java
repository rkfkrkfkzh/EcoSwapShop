package shop.ecoswapshop.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ChatSession {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID") //UUID가 16바이트, BINARY 타입 컬럼이 16바이트보다 짧게 설정되어있을수 있어 Value too long for column 에러가 생길수 있음
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
