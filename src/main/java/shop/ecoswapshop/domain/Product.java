package shop.ecoswapshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id; // 상품 아이디

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 판매자 아이디

    @Column(name = "product_name")
    private String productName; // 상품명

    @Column(name = "product_description")
    private String productDescription; // 상품 설명

    @Column(name = "price")
    private int price; // 상품가격

    @Enumerated(EnumType.STRING)
    private Condition productCondition; //상품 상태

    @ManyToOne(fetch = LAZY) // 상품과 카테고리 테이블 간의 다대일 관계 설다
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리

    private LocalDateTime creationDate; // 등록일

    @OneToMany(mappedBy = "product")
    private List<Photo> photoList = new ArrayList<>();

    // ==연관관계 메서드==
    public void setMember(Member member) {
        this.member  = member;
        member.getProductList().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getProductList().add(this);
    }

    public void addPhoto(Photo photo) {
        photoList.add(photo);
        photo.setProduct(this);
    }


}
