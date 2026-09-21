package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.SummaryResponse;
import com.financetracker.finance_tracker.dto.TransactionRequest;
import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.entity.TransactionType;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public List<Transaction> getAll(TransactionType type, Long categoryId) {
        if (type != null) {
            return transactionRepository.findByType(type);
        } else if (categoryId != null) {
            return transactionRepository.findByCategoryId(categoryId);
        } else {
            return transactionRepository.findAll();
        }
    }

    public Transaction getById(Long id){
        return transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction create(TransactionRequest transactionRequest){
        Transaction transaction = new Transaction();

        transaction.setCategory(categoryRepository.findById(transactionRequest.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setDate(transactionRequest.getDate());
        transaction.setType(transactionRequest.getType());

        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, TransactionRequest transactionRequest){
        Transaction existingTransaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        existingTransaction.setAmount(transactionRequest.getAmount());
        existingTransaction.setCategory(categoryRepository.findById(transactionRequest.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        existingTransaction.setDescription(transactionRequest.getDescription());
        existingTransaction.setDate(transactionRequest.getDate());
        existingTransaction.setType(transactionRequest.getType());
        return transactionRepository.save(existingTransaction);
    }

    public void delete(Long id){
        Transaction existing = transactionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(existing);
    }

    public BigDecimal getSummary(TransactionType type){
        BigDecimal raw = transactionRepository.sumAmountByType(type);
        BigDecimal value = raw != null ? raw : BigDecimal.ZERO;
        return value;
    }

    public SummaryResponse financeSummary(){
        SummaryResponse response = new SummaryResponse();
        response.setTotalIncome(getSummary(TransactionType.INCOME));
        response.setTotalExpense(getSummary(TransactionType.EXPENSE));
        response.setBalance(response.getTotalIncome().subtract(response.getTotalExpense()));
        return response;
    }
}
