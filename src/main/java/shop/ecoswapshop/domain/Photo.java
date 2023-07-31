package shop.ecoswapshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private Long id; // 사진 아이디

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 상품 아이디

    private String url; // 사진 url
}
