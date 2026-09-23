package com.financetracker.finance_tracker.repository;

import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.util.Date;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByType(TransactionType Type, Pageable pageable);

    Page<Transaction> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Transaction> findByDate(Date date, Pageable pageable);

    Page<Transaction> findByAmount(BigDecimal amount, Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :paramName")
    public BigDecimal sumAmountByType(@Param("paramName")TransactionType type);
}
