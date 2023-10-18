package shop.ecoswapshop.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
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
    private MultipartFile photo; // 새로 추가한 필드
    private Long categoryId; // 카테고리 선택을 위한 필드
    private boolean deleteCurrentImage = false;

}