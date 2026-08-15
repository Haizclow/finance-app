package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    public List<Transaction> getAll(){
        return repository.findAll();
    }

    public Transaction getById(Long id){
        return repository.findById(id).orElseThrow();
    }

    public Transaction create(Transaction T){
        return repository.save(T);
    }

    public Transaction update(Long id, Transaction t){
        Transaction existing = repository.findById(id).orElseThrow();
        existing.setAmount(t.getAmount());
        existing.setCategory(t.getCategory());
        existing.setDescription(t.getDescription());
        existing.setDate(t.getDate());
        existing.setType(t.getType());
        return repository.save(existing);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}
