package shop.ecoswapshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String senderId;
    private String receiverId;
    private String content;
}
