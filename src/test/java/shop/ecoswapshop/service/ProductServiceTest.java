package shop.ecoswapshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import shop.ecoswapshop.domain.Category;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.PhotoRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void registerProduct() throws IOException {
        // Given
        Product product = new Product();
        product.setId(1L);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Long productId = productService.registerProduct(product);

        // Then
        assertNotNull(productId);
        assertEquals(1L, productId);
    }

    @Test
    void findProductById() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        Optional<Product> foundProduct = productService.findProductById(productId);

        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals(productId, foundProduct.get().getId());
    }

    @Test
    void deleteProductById() {
        // Given
        Long productId = 1L;
        doNothing().when(productRepository).deleteById(productId);

        // When
        productService.deleteProductById(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void updateProduct() {
        // Given
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setProductName("Updated Name");

        // When
        productService.updateProduct(updatedProduct);

        // Then
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertEquals("Updated Name", capturedProduct.getProductName());
    }

    @Test
    void searchProducts() {
        // Given
        String keyword = "testProduct";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> expectedPage = new PageImpl<>(Collections.singletonList(new Product()));
        when(productRepository.findByProductNameContaining(keyword, pageable)).thenReturn(expectedPage);

        // When
        Page<Product> resultPage = productService.searchProducts(keyword, pageable);

        // Then
        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
    }

    @Test
    void searchProductsByCategory() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> expectedPage = new PageImpl<>(Collections.singletonList(new Product()));

        when(categoryService.findById(categoryId)).thenReturn(category);
        when(productRepository.findByCategory(any(Category.class), eq(pageable))).thenReturn(expectedPage);

        // When
        Page<Product> resultPage = productService.searchProductsByCategory(categoryId, pageable);

        // Then
        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
    }

    @Test
    void getPagedProductsByCategory() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        int page = 0;
        int size = 10;
        Sort sortOrder = Sort.by("id");
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Product> expectedPage = new PageImpl<>(Collections.singletonList(new Product()));

        when(categoryService.findById(categoryId)).thenReturn(category);
        when(productRepository.findByCategory(any(Category.class), eq(pageable))).thenReturn(expectedPage);

        // When
        Page<Product> resultPage = productService.getPagedProductsByCategory(categoryId, page, size, sortOrder);

        // Then
        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
    }

    @Test
    void getPagedProducts() {
        // Given
        int page = 0;
        int size = 10;
        Sort sortOrder = Sort.by("id");
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Product> expectedPage = new PageImpl<>(Collections.singletonList(new Product()));
        when(productRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Product> resultPage = productService.getPagedProducts(page, size, sortOrder);

        // Then
        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
    }

    @Test
    void testGetPagedProducts() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> expectedPage = new PageImpl<>(Collections.singletonList(new Product()));
        when(productRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Product> resultPage = productService.getPagedProducts(page, size);

        // Then
        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
    }

    @Test
    void getProductsByMemberId() {
        // Given
        Long memberId = 1L;
        int page = 0;
        int size = 10;
        Sort sortOrder = Sort.by("id");
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Product> expectedPage = new PageImpl<>(Collections.singletonList(new Product()));
        when(productRepository.findByMemberId(memberId, pageable)).thenReturn(expectedPage);

        // When
        Page<Product> resultPage = productService.getProductsByMemberId(memberId, page, size, sortOrder);

        // Then
        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
    }

}