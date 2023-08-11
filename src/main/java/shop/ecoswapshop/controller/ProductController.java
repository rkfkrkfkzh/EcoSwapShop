package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("productForm", new ProductForm());
        return "products/createProductForm";
    }

    @PostMapping
    public String create(@ModelAttribute ProductForm productForm) {
        Product product = new Product();
        productService.registerProduct(product);
        return "redirect:/products";
    }
}
