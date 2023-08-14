package shop.ecoswapshop.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Getter
@Setter
public class ProductForm {

    private String productName;
    private int productPrice;
    private String productDescription;
    private String productCondition;
    private String productPhoto;
}
