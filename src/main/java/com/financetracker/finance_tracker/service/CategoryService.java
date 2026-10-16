package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.CategoryRequest;
import com.financetracker.finance_tracker.dto.CategoryResponse;
import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    private CategoryResponse toResponse (Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }

    public List<CategoryResponse> getAll(){
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CategoryResponse getById(Long id) {
        return toResponse(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    public CategoryResponse update (Long id, CategoryRequest request) {
        Category existingCategory = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        existingCategory.setName(request.getName());
        return toResponse(repository.save(existingCategory));
    }

    public void delete (Long id){
        Category existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        repository.delete(existing);
    }

    public CategoryResponse create(CategoryRequest categoryRequest){
        Category category = new Category();
        category.setName(categoryRequest.getName());

        return toResponse(repository.save(category));
    }
}
