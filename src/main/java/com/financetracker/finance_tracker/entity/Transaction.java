package com.financetracker.finance_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Positive
    BigDecimal amount;

    @NotNull
    @ManyToOne
    Category category;

    @NotBlank
    String description;

    @NotNull
    LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    TransactionType type;
}
