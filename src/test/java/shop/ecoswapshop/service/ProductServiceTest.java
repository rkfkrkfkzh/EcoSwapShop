package shop.ecoswapshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.ProductRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Test
    public void 상품_등록_테스트() throws Exception{
        // given
        Product product = new Product();
        product.setProductName("상품1");
        product.setPrice(10000);
        // ... 상품 정보 설정 ...

        // when
        Long savedProductId = productService.registerProduct(product);

        // then
        assertNotNull(savedProductId);
    }

}