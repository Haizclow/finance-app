package com.financetracker.finance_tracker.repository;

import com.financetracker.finance_tracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
