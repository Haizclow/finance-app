package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.SummaryResponse;
import com.financetracker.finance_tracker.dto.TransactionRequest;
import com.financetracker.finance_tracker.dto.TransactionResponse;
import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.entity.TransactionType;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;


    private TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setCategoryName(transaction.getCategory().getName());
        transactionResponse.setCategoryId(transaction.getCategory().getId());
        transactionResponse.setDescription(transaction.getDescription());
        transactionResponse.setDate(transaction.getDate());
        transactionResponse.setType(transaction.getType());

        return transactionResponse;
    }


    public Page<TransactionResponse> getAll
            (
                    Pageable pageable,
                    TransactionType type,
                    Long categoryId
            ) {
        if ((type != null) && (categoryId != null)) {
            return transactionRepository.findByTypeAndCategoryId(type, categoryId, pageable).map(this::toResponse);
        } else if (type != null) {
            return transactionRepository.findByType(type, pageable).map(this::toResponse);
        } else if (categoryId != null) {
            return transactionRepository.findByCategoryId(categoryId, pageable).map(this::toResponse);
        } else return transactionRepository.findAll(pageable).map(this::toResponse);
    }

    public TransactionResponse getById(Long id) {
        return toResponse(findTransactionOrThrow(id));
    }

    public TransactionResponse create(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();

        transaction.setCategory(findCategoryOrThrow(transactionRequest.getCategoryId()));
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setDate(transactionRequest.getDate());
        transaction.setType(transactionRequest.getType());

        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse update(Long id, TransactionRequest transactionRequest) {
        Transaction existingTransaction = findTransactionOrThrow(id);
        existingTransaction.setAmount(transactionRequest.getAmount());
        existingTransaction.setCategory(findCategoryOrThrow(transactionRequest.getCategoryId()));
        existingTransaction.setDescription(transactionRequest.getDescription());
        existingTransaction.setDate(transactionRequest.getDate());
        existingTransaction.setType(transactionRequest.getType());
        return toResponse(transactionRepository.save(existingTransaction));
    }

    public void delete(Long id) {
        Transaction existing = findTransactionOrThrow(id);
        transactionRepository.delete(existing);
    }

    public BigDecimal getSummary(TransactionType type) {
        BigDecimal raw = transactionRepository.sumAmountByType(type);
        BigDecimal value = raw != null ? raw : BigDecimal.ZERO;
        return value;
    }

    public SummaryResponse financeSummary() {
        SummaryResponse response = new SummaryResponse();
        response.setTotalIncome(getSummary(TransactionType.INCOME));
        response.setTotalExpense(getSummary(TransactionType.EXPENSE));
        response.setBalance(response.getTotalIncome().subtract(response.getTotalExpense()));
        return response;
    }

    private Transaction findTransactionOrThrow(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Category not found"));
    }
}
