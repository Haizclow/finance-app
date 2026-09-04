package com.financetracker.finance_tracker.controller;

import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category C) {
        return ResponseEntity.status(201).body(service.create(C));
    }
}
