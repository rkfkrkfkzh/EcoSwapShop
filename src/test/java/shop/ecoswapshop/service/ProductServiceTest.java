package shop.ecoswapshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductService productService;

    @Test
    @Rollback(value = false)
    public void 상품_등록() throws Exception {
        // Given
        Product product = new Product();
        product.setProductName("새로운 상품");
        product.setPrice(1000);

        // When
        List<MultipartFile> files = null;
        Long productId = productService.registerProduct(product);

        // Then
        assertNotNull(productId);
        assertEquals(product.getId(), productId);
    }

    @Test
    public void 상품_조회() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setProductName("테스트 상품");
        product.setPrice(2000);

        Product saveProduct = productRepository.save(product);

        // When
        Optional<Product> foundProduct = productService.findProductById(saveProduct.getId());
        // Then
        assertTrue(foundProduct.isPresent()); // 객체 값이 있을 경우 true 반환(Optional -> isPresent 정의)
        assertEquals(saveProduct, foundProduct.get());

        productService.deleteAllProducts();
        foundProduct = productService.findProductById(productId);

        // Then
        assertFalse(foundProduct.isPresent()); // 객체 값이 없을 경우 false 반환(Optional isPresent 정의)
    }

    @Test
    public void 전체_상품_조회() {
        // Given
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "상품1", 1000));
        productList.add(new Product(2L, "상품2", 2000));
        productList.add(new Product(3L, "상품3", 3000));

        productRepository.saveAll(productList);

        // When
        List<Product> foundProducts = productService.findAllProducts();

        // Then
        assertFalse(foundProducts.isEmpty()); // 컬렉션 요소가 없을경우 true 반환 현재는 요소가 존재해서 false
        assertEquals(productList.size(), foundProducts.size());
        assertEquals(productList, foundProducts);

        productService.deleteAllProducts();
        foundProducts = productService.findAllProducts();
        // Then
        assertTrue(foundProducts.isEmpty());
    }

    @Test
    public void 상품_삭제() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setProductName("삭제할 상품");
        product.setPrice(20000);

        productRepository.save(product);

        // When
        productService.deleteProductById(productId);

        // Then
        Optional<Product> foundProduct = productService.findProductById(productId);
        assertFalse(foundProduct.isPresent());
    }

    @Test
    public void 특정상품_사진추가() throws IOException {
        // Given
        Product product = new Product(); // 상품 생성 로직에 맞게 조정해야 함.
        Long productId = productRepository.save(product).getId();

        // When
        List<MultipartFile> files = null;
        Long photoId = productService.registerProduct(product);

        // Then
        assertNotNull(photoId);
        assertEquals(1, productService.getPhotoByProductId(productId).size());
    }

    @Test
    public void 특정상품_모든사진조회() throws IOException {
        // Given
        Product product = new Product(); // 상품 생성 로직에 맞게 조정해야 함.
        Long productId = productRepository.save(product).getId();

        List<MultipartFile> files = null;
        productService.registerProduct(product);

        // When
        List<Photo> photos = productService.getPhotoByProductId(productId);

        // Then
        assertEquals(1, photos.size());
    }

    @Test
    public void 특정상품_사진제거() throws IOException {
        // Given
        Product product = new Product(); // 상품 생성 로직에 맞게 조정해야 함.
        Product product1 = productRepository.save(product);
        List<MultipartFile> files = null;
        Long product2 = productService.registerProduct(product1);

        // When
        productService.deleteProductById(product2);

        // Then
        assertEquals(0, productService.getPhotoByProductId(product2).size());
    }

}