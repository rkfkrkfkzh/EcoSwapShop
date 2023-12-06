package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Category;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.exception.NotFoundException;
import shop.ecoswapshop.repository.PhotoRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    // 상품 등록
    @Transactional
    public Long registerProduct(Product product) throws IOException {

        return productRepository.save(product).getId();
    }

    // 상품 조회
    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // 상품 삭제
    @Transactional
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public void updateProduct(Product product) {
        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            existingProduct.setProductName(product.getProductName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setProductDescription(product.getProductDescription());
            existingProduct.setProductCondition(product.getProductCondition());

            productRepository.save(existingProduct);
        } else {
            throw new NotFoundException("Product with id " + product.getId());
        }
    }

    // 검색
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByProductNameContaining(keyword, pageable);
    }

    public Page<Product> searchProductsByCategory(Long categoryId, Pageable pageable) {
        // 카테고리를 가져오는 부분
        Category category = categoryService.findById(categoryId);
        return productRepository.findByCategory(category, pageable);
    }

    public Page<Product> getPagedProductsByCategory(Long categoryId, int page, int size, Sort sortOrder) {
        Category category = categoryService.findById(categoryId);
        return productRepository.findByCategory(category, PageRequest.of(page, size, sortOrder));
    }

    // 페이징 처리
    public Page<Product> getPagedProducts(int page, int pageSize, Sort sort) {
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return productRepository.findAll(pageable);
    }

    // 페이징 처리
    public Page<Product> getPagedProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByMemberId(Long memberId, int page, int pageSize, Sort sortOrder) {
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);
        return productRepository.findByMemberId(memberId, pageable);
    }
}



