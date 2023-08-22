package shop.ecoswapshop.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.internal.LoadingCache;
import shop.ecoswapshop.domain.Condition;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductForm {

    private String productName;
    private int price;
    private String productDescription;
    private Condition productCondition;
    private String photoList;
    private LocalDateTime creationDate;

}
