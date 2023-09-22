package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import shop.ecoswapshop.service.PhotoService;
import shop.ecoswapshop.service.ProductService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;
    private final PhotoService photoService;

    // 로그인한 사용자의 memberId를 가져옵니다.
    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    private Member getLoggedInMember() {
        return getLoggedInMemberId()
                .map(memberService::findMemberById)
                .orElseThrow(() -> new RuntimeException("Logged in member not found"))
                .orElseThrow(() -> new RuntimeException("Member Not Found"));
    }

    private boolean isUserAuthorized(Long memberId) {
        return getLoggedInMemberId().isPresent() && getLoggedInMemberId().get().equals(memberId);

    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        // 모든 상품을 조회합니다.
        Page<Product> pagedProducts = productService.getPagedProducts(page, 8);
        // 상품 목록을 모델에 추가합니다.
        model.addAttribute("pagedProducts", pagedProducts);
        // 로그인한 사용자의 memberId가 있다면 모델에 추가합니다.
        getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));

        return "products/productList";
    }

    @GetMapping("/details/{productId}")
    public String details(@PathVariable Long productId, Model model) {
        Optional<Product> product = productService.findProductById(productId);
        if (!product.isPresent()) {
            return "redirect:/error";
        }
        model.addAttribute("product", product.get());
        // 이 부분에서 해당 상품에 연결된 이미지 정보를 불러옵니다.
        List<String> imageUrls = photoService.findImageUrlsByProductId(productId);
        model.addAttribute("imageUrls", imageUrls);

        getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
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
    public String create(@ModelAttribute ProductForm productForm, @RequestParam("files") List<MultipartFile> files) throws IOException {
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

        productService.registerProduct(product, files); // 이 함수 내에서 파일 정보를 DB에 저장

        return "redirect:/products";
    }

    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable Long productId, Model model) {
        List<String> imageUrls = photoService.findImageUrlsByProductId(productId);
        model.addAttribute("imageUrls", imageUrls);
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
        Optional<Product> productById = productService.findProductById(productId);
        if (!productById.isPresent()) {
            return "redirect:/error";
        }
        Product product = productById.get();
        if (!isUserAuthorized(product.getMember().getId())) {
            return "redirect:/error";
        }
        productService.deleteProductById(productId);
        return "redirect:/products";
    }

    //이미지 수정
    @PostMapping("/edit/photo/{photoId}")
    public String editPhoto(@PathVariable Long photoId, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Optional<Photo> optionalPhoto = photoService.findPhotoById(photoId);
        if (!optionalPhoto.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Photo not found for given Id");
            return "redirect:/error";
        }

        Photo photo = optionalPhoto.get();
        if (!isUserAuthorized(photo.getProduct().getMember().getId())) {
            redirectAttributes.addFlashAttribute("message", "Not authorized to edit the photo");
            return "redirect:/error";
        }
        try {
            photoService.updatePhoto(photoId, file); // 이미지 파일 업데이트
            redirectAttributes.addFlashAttribute("message", "Photo updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Error occurred while updating photo");
            return "redirect:/error";
        }
        return "redirect:/products/details/" + photo.getProduct().getId();
    }

    // 이미지 삭제
    @PostMapping("/delete/photo/{photoId}")
    public String deletePhoto(@PathVariable Long photoId, RedirectAttributes redirectAttributes) {
        Optional<Photo> optionalPhoto = photoService.findPhotoById(photoId);
        if (!optionalPhoto.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Photo not found for given Id");
            return "redirect:/error";
        }
        Photo photo = optionalPhoto.get();
        if (!isUserAuthorized(photo.getProduct().getMember().getId())) {
            redirectAttributes.addFlashAttribute("message", "Not authorized to delete the photo");
            return "redirecct:/error";
        }
        photoService.deletePhoto(photoId);
        redirectAttributes.addFlashAttribute("message", "Photo deleted successfully!");
        return "redirect:/products/details/" + photo.getProduct().getId();
    }
}
