package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.ecoswapshop.domain.Condition;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.ProductService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;

    @GetMapping
    public String list(Model model) {
        // 로그인한 사용자의 memberId를 가져옵니다.
        Optional<Long> loggedInMemberId = memberService.findLoggedInMemberId();

        // 모든 상품을 조회합니다.
        List<Product> products = productService.findAllProducts();

        // 상품 목록을 모델에 추가합니다.
        model.addAttribute("products", products);

        // 로그인한 사용자의 memberId가 있다면 모델에 추가합니다.
        loggedInMemberId.ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));

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
        Optional<Long> optionalMemberId = memberService.findLoggedInMemberId();

        if (!optionalMemberId.isPresent()) {
            return "redirect:/error";
        }

        Long memberId = optionalMemberId.get();
        Optional<Member> optionalMember = memberService.findMemberById(memberId);

        if (!optionalMember.isPresent()) {
            return "redirect:/error";
        }

        Member member = optionalMember.get();
        Product product = new Product();
        product.setProductName(productForm.getProductName());
        product.setPrice(productForm.getPrice());
        product.setProductDescription(productForm.getProductDescription());
        product.setMember(member);
        // Product 객체와 Member 객체를 연결하는 로직 추가 (예: foreign key 설정 등)
        // 예: product.setMember(member);

        productService.registerProduct(product);

        return "redirect:/products";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("productImage") MultipartFile file, RedirectAttributes redirectAttributes) {
        // 파일 처리 로직 (저장 등)
        return "redirect:/success";
    }
}
