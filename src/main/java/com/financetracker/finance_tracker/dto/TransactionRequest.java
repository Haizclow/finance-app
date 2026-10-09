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
    private BigDecimal amount;

    @NotNull
    private Long categoryId;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate date;

    @NotNull
    private TransactionType type;
}

