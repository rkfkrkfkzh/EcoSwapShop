package shop.ecoswapshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        product.getChatMessages().add(this);
    }

}
