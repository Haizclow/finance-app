package com.financetracker.finance_tracker.controller;

import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
private final TransactionService service;

@GetMapping
public ResponseEntity<List<Transaction>> getAll() {
    return ResponseEntity.ok(service.getAll());
}

@GetMapping("/{id}")
public ResponseEntity<Transaction> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
}

@PostMapping
public ResponseEntity<Transaction> create(@RequestBody Transaction t) {
    return ResponseEntity.status(201).body(service.create(t));
}

@PutMapping("/{id}")
public ResponseEntity<Transaction> update(@RequestBody Transaction t, @PathVariable Long id) {
    return ResponseEntity.ok(service.update(id, t));
}

@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
}

}
