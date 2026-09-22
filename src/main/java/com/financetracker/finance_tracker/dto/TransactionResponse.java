package com.financetracker.finance_tracker.dto;


import com.financetracker.finance_tracker.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransactionResponse {

    Long id;

    BigDecimal amount;

    String categoryName;

    Long categoryId;

    String description;

    LocalDate date;

    TransactionType type;
}
