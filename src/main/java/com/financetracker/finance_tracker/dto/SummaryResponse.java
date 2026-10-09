package com.financetracker.finance_tracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SummaryResponse {


    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;

}
