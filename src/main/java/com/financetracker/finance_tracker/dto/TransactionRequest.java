package com.financetracker.finance_tracker.dto;

import com.financetracker.finance_tracker.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransactionRequest {

    @NotNull
    @Positive
    BigDecimal amount;

    @NotNull
    Long categoryId;

    @NotBlank
    String description;

    @NotNull
    LocalDate date;

    @NotNull
    TransactionType type;
}

