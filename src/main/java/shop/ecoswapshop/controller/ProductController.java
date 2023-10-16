package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.ecoswapshop.domain.*;
import shop.ecoswapshop.service.CategoryService;
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
    private final CategoryService categoryService;

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

    private Product getAuthorizedProduct(Long productId) {
        Product product = productService.findProductById(productId).orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        if (!isUserAuthorized(product.getMember().getId())) {
            throw new RuntimeException("수정권한이 없습니다.");
        }
        return product;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String sort,@RequestParam(required = false) Long categoryId, Model model) {
        Sort sortOrder = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            sortOrder = Sort.by(sortParams[1].equalsIgnoreCase("asc")
                    ? Sort.Order.asc(sortParams[0])
                    : Sort.Order.desc(sortParams[0]));
        }
        // 모든 상품을 조회합니다.
        Page<Product> pagedProducts;
        if (categoryId != null) {
            pagedProducts = productService.getPagedProductsByCategory(categoryId, page, 8, sortOrder);
        } else {
            pagedProducts = productService.getPagedProducts(page, 8, sortOrder);
        }
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

        getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
        return "products/productsDetails"; //Thymeleaf view
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Member loggedInMember = getLoggedInMember();
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("conditions", Condition.values()); // Condition 열거형의 값들을 모델에 추가
        // 카테고리 데이터를 가져와 모델에 추가
        model.addAttribute("categories", categoryService.findAll());

        return "products/createProductForm";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ProductForm productForm) throws IOException {
        Member loggedInMember = getLoggedInMember();
        Product product = new Product();

        product.setProductName(productForm.getProductName());
        product.setPrice(productForm.getPrice());
        product.setProductDescription(productForm.getProductDescription());
        product.setProductCondition(productForm.getProductCondition());
        product.setCreationDate(LocalDateTime.now());
        product.setMember(loggedInMember);

        // 카테고리 설정
        Long categoryId = productForm.getCategoryId();
        Category category = categoryService.findById(categoryId);
        if (category != null) {
            product.setCategory(category);
        }

        // 이미지 파일 처리 및 상품 저장
        MultipartFile productImage = productForm.getPhoto();
        if (productImage != null && !productImage.isEmpty()) {
            if (!productImage.getContentType().startsWith("image/")) {
                throw new RuntimeException("업로드한 이미지 파일이 아닙니다.");
            }
            String fileName = photoService.storeFile(productImage);
            String imagePath = "/uploads/" + fileName;

            Photo photo = new Photo();
            photo.setUrl(imagePath);

            product.addPhoto(photo);
        }
        productService.registerProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable Long productId, Model model) {
        return processEditForm(productId, model);
    }

    private String processEditForm(Long productId, Model model) {
        Product product = getAuthorizedProduct(productId);

        // 카테고리 목록을 모델에 추가
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("productForm", product);
        model.addAttribute("conditions", Condition.values());
        return "products/productEdit";
    }

    @PostMapping("/edit/{productId}")
    public String edit(@PathVariable Long productId, @ModelAttribute ProductForm productForm) throws IOException {

        return processEdit(productId, productForm);
    }

    private String processEdit(Long productId, ProductForm productForm) throws IOException {
        Product product = getAuthorizedProduct(productId);
        // 상품 기본정보 업데이트
        product.setProductName(productForm.getProductName());
        product.setProductDescription(productForm.getProductDescription());
        product.setPrice(productForm.getPrice());
        product.setProductCondition(productForm.getProductCondition());
        // 카테고리 설정
        Long categoryId = productForm.getCategoryId();
        Category category = categoryService.findById(categoryId);
        if (category != null) {
            product.setCategory(category);
        }
        // 이미지 업로드 및 기존 이미지 삭제 로직 추가
        MultipartFile newProductImage = productForm.getPhoto();
        if (newProductImage != null && !newProductImage.isEmpty()) {
            if (!newProductImage.getContentType().startsWith("image/")) {
                throw new RuntimeException("업로드한 파일이 이미지가 아닙니다.");
            }
            // 기존에 연결된 사진 삭제
            for (Photo oldPhoto : product.getPhotoList()) {
                photoService.deletePhoto(oldPhoto.getId());
            }
            product.getPhotoList().clear();

            // 새로운 사진 업로드
            String fileName = photoService.storeFile(newProductImage);
            String imagePath = "/uploads/" + fileName;
            Photo newPhoto = new Photo();
            newPhoto.setUrl(imagePath);

            product.addPhoto(newPhoto);
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

    // 상품 삭제
    @GetMapping("delete/{productId}")
    public String delete(@PathVariable Long productId) {
        Product product = getAuthorizedProduct(productId);
        productService.deleteProductById(productId);
        return "redirect:/products";
    }

    // 상품 검색
    @GetMapping("/search")
    public String search(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false)Long categoryId, Model model) {
        Page<Product> searchProducts = productService.searchProducts(keyword, PageRequest.of(page, 8));
        if (categoryId != null) {
            // categoryId를 이용한 검색 로직
            searchProducts = productService.searchProductsByCategory(categoryId, PageRequest.of(page, 8));
        } else {
            searchProducts = productService.searchProducts(keyword, PageRequest.of(page, 8));
        }
        model.addAttribute("pagedProducts", searchProducts);
        model.addAttribute("keyword", keyword);
        getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
        return "products/productList";

    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.findAll();
    }

}
