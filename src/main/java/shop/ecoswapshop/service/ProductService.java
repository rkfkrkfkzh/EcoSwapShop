package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.dom4j.XPathException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.ecoswapshop.domain.Photo;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.exception.NotFoundException;
import shop.ecoswapshop.repository.PhotoRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public Long registerProduct(Product product, List<MultipartFile> files) throws IOException {

        Product savedProduct = productRepository.save(product);

        File directory = new File("uploads");
        if (!directory.exists()) {
            directory.mkdir(); //디렉터리 생성
        }
        for (MultipartFile file :files){
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + file.getOriginalFilename());

            Files.write(path, bytes);

            String photoUrl = "/uploads/" + file.getOriginalFilename();
            Photo photo = new Photo();
            photo.setUrl(photoUrl);
            photo.setProduct(savedProduct);
            photoRepository.save(photo);
        }
        return savedProduct.getId();
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

    // 특정 상품의 모든 사진 조회
    public List<Photo> getPhotoByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return product.getPhotoList();
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

    // 페이징 처리
    public Page<Product> getPagedProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findAll(pageable);
    }

}



