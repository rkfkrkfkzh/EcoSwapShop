package shop.ecoswapshop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

    private String productName;
    private int price;
    private String productDescription;
    private String productCondition;
    private String photoList;
}
