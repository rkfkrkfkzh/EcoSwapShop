package shop.ecoswapshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.ecoswapshop.domain.Category;
import shop.ecoswapshop.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAll() {
        // Given
        List<Category> expectedCategories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // When
        List<Category> categories = categoryService.findAll();

        // Then
        assertNotNull(categories);
        assertEquals(expectedCategories, categories);
    }

    @Test
    void save() {
        // Given
        Category category = new Category();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        Category savedCategory = categoryService.save(category);

        // Then
        assertNotNull(savedCategory);
        verify(categoryRepository).save(category);
    }

    @Test
    void findById() {
        // Given
        Long categoryId = 1L;
        Category expectedCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        // When
        Category category = categoryService.findById(categoryId);

        // Then
        assertEquals(expectedCategory, category);
    }

    @Test
    void delete() {
        // Given
        Long categoryId = 1L;
        doNothing().when(categoryRepository).deleteById(categoryId);

        // When
        categoryService.delete(categoryId);

        // Then
        verify(categoryRepository).deleteById(categoryId);
    }
}