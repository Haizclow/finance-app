package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.CategoryRequest;
import com.financetracker.finance_tracker.dto.CategoryResponse;
import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.exception.CategoryInUseException;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    private CategoryResponse toResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CategoryResponse getById(Long id) {
        return toResponse(findCategoryOrThrow(id));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category existingCategory = findCategoryOrThrow(id);
        existingCategory.setName(request.getName());
        return toResponse(categoryRepository.save(existingCategory));
    }

    public void delete(Long id) {
        Category existing = findCategoryOrThrow(id);
        boolean isUsed = transactionRepository.existsByCategoryId(id);
        if (isUsed) {
            throw new CategoryInUseException("This category is used in transactions");
        }
        categoryRepository.delete(existing);
    }

    public CategoryResponse create(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());

        return toResponse(categoryRepository.save(category));
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Category not found"));
    }
}
