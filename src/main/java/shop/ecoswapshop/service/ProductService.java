package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.exception.ProductNotFoundException;
import shop.ecoswapshop.repository.PhotoRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PhotoRepository photoRepository;

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

    // 상품 모두 삭제
    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    // 특정상품에 사진추가
    @Transactional
    public Long addPhotoToProduct(Long productId, Photo photo) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.addPhoto(photo);
        photoRepository.save(photo);
        return photo.getId();
    }

    // 특정 상품의 모든 사진 조회
    public List<Photo> getPhotoByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return product.getPhotoList();
    }

    //특정 상품에서 특정 사진을 제거
    @Transactional
    public void removePhotoFromProduct(Long productId, Long photoId) {
        Product product = productRepository.findById(productId).orElseThrow();
        Photo photoToRemove = product.getPhotoList().stream() // 리스트를 스트림으로 변환, 코드의 가독성, 유지보수성, 확장성, 성능 최적화
                .filter(photo -> photo.getId().equals(photoId))// photoId와 같은 요소만 선택
                .findFirst() // Optional<Photo> 타입을 반환
                .orElseThrow(); // 빈 Optional을 반환하면 예외처리
        product.getPhotoList().remove(photoToRemove);
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
            throw new ProductNotFoundException("Product with id " + product.getId());
        }
    }
}



