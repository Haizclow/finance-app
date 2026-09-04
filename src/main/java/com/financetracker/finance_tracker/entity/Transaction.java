package com.financetracker.finance_tracker.entity;

import jakarta.persistence.*;
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

    BigDecimal amount;
    @ManyToOne
    Category category;
    String description;
    LocalDate date;

    @Enumerated(EnumType.STRING)
    TransactionType type;
}
