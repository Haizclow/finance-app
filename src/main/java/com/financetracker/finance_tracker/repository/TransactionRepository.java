package com.financetracker.finance_tracker.repository;

import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByType(TransactionType Type);

    List<Transaction> findByCategoryId(Long categoryId);
}
