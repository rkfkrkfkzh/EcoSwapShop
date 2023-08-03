package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;



    // 상품 등록
    @Transactional
    public Long registerProduct(Product product) {
        // 상품 등록 로직
        // ...

        // 상품을 저장하고 생성된 상품의 ID를 반환
        return productRepository.save(product).getId();
    }

    // 상품 조회
    public Optional<Product> findProductById(Long productId) {
        // 상품 ID로 상품 조회 로직
        // ...

        return productRepository.findById(productId);
    }

    // 전체 상품 조회
    public List<Product> findAllProducts() {
        // 모든 상품 조회 로직
        // ...

        return productRepository.findAll();
    }

    // 상품 삭제
    @Transactional
    public void deleteProductById(Long productId) {
        // 상품 삭제 로직
        // ...

        productRepository.deleteById(productId);
    }

    // 기타 필요한 상품 조회 메서드들을 추가로 정의할 수 있습니다.
}


