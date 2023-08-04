package shop.ecoswapshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.ProductRepository;

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
        assertTrue(foundProduct.isPresent());
        assertEquals(saveProduct, foundProduct.get());

        productService.deleteAllProducts();
        foundProduct = productService.findProductById(productId);
        // Then
        assertFalse(foundProduct.isPresent());
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
        assertFalse(foundProducts.isEmpty());
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

}