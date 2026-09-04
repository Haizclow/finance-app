package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public List<Category> getAll(){
        return repository.findAll();
    }

    public Category create(Category C){
        return repository.save(C);
    }
}
