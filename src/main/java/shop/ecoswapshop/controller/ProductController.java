package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.ecoswapshop.domain.Condition;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;


    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    private Member getLoggedInMember() {
        Optional<Long> loggedInMemberId = getLoggedInMemberId();
        if (!loggedInMemberId.isPresent()) {
            throw new RuntimeException("Logged in member not found");
        }
        Long memberId = loggedInMemberId.get();
        Optional<Member> memberById = memberService.findMemberById(memberId);
        if (!memberById.isPresent()) {
            throw new RuntimeException("Member Not Found");
        }
        return memberById.get();
    }

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

    @GetMapping("/details/{productId}")
    public String details(@PathVariable Long productId, Model model) {
        Optional<Product> product = productService.findProductById(productId);
        if (!product.isPresent()) {
            return "redirect:/error";
        }
        model.addAttribute("product", product.get());
        return "products/productsDetails"; //Thymeleaf view
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Member loggedInMember = getLoggedInMember();
        Product product = new Product();
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("product", product);
        model.addAttribute("conditions", Condition.values()); // Condition 열거형의 값들을 모델에 추가

        return "products/createProductForm";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ProductForm productForm) {
        Member loggedInMember = getLoggedInMember();
        Product product = new Product();
        product.setProductName(productForm.getProductName());
        product.setPrice(productForm.getPrice());
        product.setProductDescription(productForm.getProductDescription());
        product.setProductCondition(productForm.getProductCondition());
        product.setCreationDate(LocalDateTime.now());
        product.setMember(loggedInMember);
        // Product 객체와 Member 객체를 연결하는 로직 추가 (예: foreign key 설정 등)
        // 예: product.setMember(member);

        productService.registerProduct(product);

        return "redirect:/products";
    }

    private boolean isUserAuthorized(Long memberId) {
        Optional<Long> loggedInMemberId = memberService.findLoggedInMemberId();
        return loggedInMemberId.isPresent() && loggedInMemberId.get().equals(memberId);

    }

    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable Long productId, Model model) {
        return processEditForm(productId, model);
    }

    private String processEditForm(Long productId, Model model) {
        Optional<Product> product = productService.findProductById(productId);
        if (!product.isPresent()) {
            return "redirect:/error";
        }
        // 추가: 현재 로그인한 사용자의 정보 가져오기
        if (!isUserAuthorized(product.get().getMember().getId())) {
            return "redirect:/error";
        }

        model.addAttribute("productForm", product.get());
        model.addAttribute("conditions", Condition.values());
        return "products/productEdit";
    }

    @PostMapping("/edit/{productId}")
    public String edit(@PathVariable Long productId, @ModelAttribute ProductForm productForm) {
        return processEdit(productId, productForm);
    }

    private String processEdit(Long productId, ProductForm productForm) {
        Optional<Product> productById = productService.findProductById(productId);
        if (!productById.isPresent()) {
            return "redirect:/error";
        }
        Product product = productById.get();
        if (!isUserAuthorized(product.getMember().getId())) {
            return "redirect:/error";
        }
        product.setProductName(productForm.getProductName());
        product.setProductDescription(productForm.getProductDescription());
        product.setPrice(productForm.getPrice());
        product.setProductCondition(productForm.getProductCondition());

        productService.updateProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("productImage") MultipartFile file, RedirectAttributes redirectAttributes) {
        // 파일 처리 로직 (저장 등)
        return "redirect:/success";
    }

    @GetMapping("delete/{productId}")
    public String delete(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return "redirect:/products";
    }
}
