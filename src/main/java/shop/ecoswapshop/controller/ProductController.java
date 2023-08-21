package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.ecoswapshop.domain.Condition;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String list(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "products/productList";
    }

    @GetMapping("/{productId}")
    public String detail(@PathVariable Long productId, Model model) {
        Product product = productService.findProductById(productId).orElseThrow();
        List<Photo> photos = productService.getPhotoByProductId(productId);
        model.addAttribute("product", product);
        model.addAttribute("photos", photos);
        return "products/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Product product = new Product();
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("product", product);
        model.addAttribute("conditions", Condition.values()); // Condition 열거형의 값들을 모델에 추가

        return "products/createProductForm";
    }


    @PostMapping("/create")
    public String create(@ModelAttribute ProductForm productForm) {
        Product product = new Product();
        productService.registerProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("productImage") MultipartFile file, RedirectAttributes redirectAttributes) {
        // 파일 처리 로직 (저장 등)
        return "redirect:/success";
    }
}
