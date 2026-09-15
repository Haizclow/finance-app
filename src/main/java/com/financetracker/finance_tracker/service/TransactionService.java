package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.entity.TransactionType;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    public List<Transaction> getAll(TransactionType type, Long categoryId) {
        if (type != null) {
            return repository.findByType(type);
        } else if (categoryId != null) {
            return repository.findByCategoryId(categoryId);
        } else {
            return repository.findAll();
        }
    }

    public Transaction getById(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction create(Transaction T){
        return repository.save(T);
    }

    public Transaction update(Long id, Transaction t){
        Transaction existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        existing.setAmount(t.getAmount());
        existing.setCategory(t.getCategory());
        existing.setDescription(t.getDescription());
        existing.setDate(t.getDate());
        existing.setType(t.getType());
        return repository.save(existing);
    }

    public void delete(Long id){
        Transaction existing = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        repository.delete(existing);
    }
}
