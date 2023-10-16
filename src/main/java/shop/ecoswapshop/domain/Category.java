package shop.ecoswapshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id; // 카테고리 번호

    private String name; // 카테고리 이름

    // 카테고리와 상품 테이블 간의 일대다 관계 설정
    @OneToMany(mappedBy = "category")
    private List<Product> productList = new ArrayList<>(); // 상품리스트

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public Category() {
    }

    // ==연관관계 메서드==
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
