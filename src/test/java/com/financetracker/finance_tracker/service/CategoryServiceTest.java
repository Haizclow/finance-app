package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.CategoryResponse;
import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void getAll_shouldReturnAllCategories_inRepository (){
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


    }
}
