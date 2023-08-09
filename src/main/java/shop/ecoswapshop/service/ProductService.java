package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    public Long registerProduct(Product product) {
        return productRepository.save(product).getId();
    }

    // 상품 조회
    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // 전체 상품 조회
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    // 상품 삭제
    @Transactional
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
    // 기타 필요한 상품 조회 메서드들을 추가로 정의할 수 있습니다.
}


