package com.financetracker.finance_tracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SummaryResponse {


    BigDecimal totalIncome;

    BigDecimal totalExpense;

    BigDecimal balance;

}
