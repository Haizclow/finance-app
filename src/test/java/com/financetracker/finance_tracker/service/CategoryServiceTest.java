package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.CategoryRequest;
import com.financetracker.finance_tracker.dto.CategoryResponse;
import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.exception.CategoryInUseException;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void getAll_shouldReturnAllCategories_inRepository() {
        Category category = new Category();
        Category category2 = new Category();

        category.setId(1L);
        category.setName("Еда");
        category2.setId(2L);
        category2.setName("Такси");


        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category, category2));

        List<CategoryResponse> response = categoryService.getAll();

        Assertions.assertEquals(1L, response.get(0).getId());
        Assertions.assertEquals("Еда", response.get(0).getName());

        Assertions.assertEquals(2L, response.get(1).getId());
        Assertions.assertEquals("Такси", response.get(1).getName());

        Assertions.assertEquals(2, response.size());
    }

    @Test
    void getAll_shouldReturnBlankList_whenRepositoryIsEmpty() {


        Mockito.when(categoryRepository.findAll()).thenReturn(List.of());

        List<CategoryResponse> response = categoryService.getAll();

        Assertions.assertEquals(new ArrayList<>(), response);

    }

    @Test
    void create_shouldReturnCategory_whenItCreated() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Еда");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Еда");

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse created = categoryService.create(request);
        //checks what is being returned after creating the Category
        Assertions.assertEquals(1L, created.getId());
        Assertions.assertEquals("Еда", created.getName());

        //captures what went into save() and checks if save() was called only once
        Mockito.verify(categoryRepository, Mockito.times(1)).save(captor.capture());

        Category passed = captor.getValue();

        Assertions.assertEquals("Еда", passed.getName());
        Assertions.assertNull(passed.getId());
    }

    @Test
    void delete_shouldThrowCategoryInUse_whenCategoryHasTransactions() {
        Category category = new Category();

        category.setId(1L);
        category.setName("Еда");

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Mockito.when(transactionRepository.existsByCategoryId(1L)).thenReturn(true);

        Assertions.assertThrows(CategoryInUseException.class, () -> {
            categoryService.delete(1L);
        });

        Mockito.verify(categoryRepository, Mockito.never()).delete(any(Category.class));
    }

    @Test
    void update_shouldUpdateAndSave_whenCategoryExists() {
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        Category existingCategory = new Category();
        CategoryRequest request = new CategoryRequest();

        ReflectionTestUtils.setField(existingCategory, "id", 1L);
        ReflectionTestUtils.setField(existingCategory, "name", "Еда");

        ReflectionTestUtils.setField(request, "name", "Такси");

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        CategoryResponse response = categoryService.update(1L, request);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Такси", response.getName());

        Mockito.verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        Assertions.assertEquals(1L, capturedCategory.getId());
        Assertions.assertEquals("Такси", capturedCategory.getName());
    }

    @Test
    void update_shouldThrowException_whenCategoryNotExist() {
        CategoryRequest request = new CategoryRequest();

        ReflectionTestUtils.setField(request, "name", "Такси");

        Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(2L, request);
        });

        Mockito.verify(categoryRepository, Mockito.never()).save(any(Category.class));
    }


}
