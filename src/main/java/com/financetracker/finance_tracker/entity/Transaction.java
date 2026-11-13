package com.financetracker.finance_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @ManyToOne
    private Category category;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
