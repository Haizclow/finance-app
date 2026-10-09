package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.CategoryRequest;
import com.financetracker.finance_tracker.dto.CategoryResponse;
import com.financetracker.finance_tracker.entity.Category;
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

    public CategoryResponse create(CategoryRequest categoryRequest){
        Category category = new Category();
        category.setName(categoryRequest.getName());

        return toResponse(repository.save(category));
    }
}
