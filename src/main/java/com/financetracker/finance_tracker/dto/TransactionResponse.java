package com.financetracker.finance_tracker.dto;


import com.financetracker.finance_tracker.entity.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransactionResponse {

    private Long id;

    private BigDecimal amount;

    private String categoryName;

    private Long categoryId;

    private String description;

    private LocalDate date;

    private TransactionType type;
}
