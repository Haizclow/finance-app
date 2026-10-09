package com.financetracker.finance_tracker.controller;

import com.financetracker.finance_tracker.dto.CategoryRequest;
import com.financetracker.finance_tracker.dto.CategoryResponse;
import com.financetracker.finance_tracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(categoryRequest));
    }
}
