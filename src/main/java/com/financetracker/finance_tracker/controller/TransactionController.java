package com.financetracker.finance_tracker.controller;

import com.financetracker.finance_tracker.dto.SummaryResponse;
import com.financetracker.finance_tracker.dto.TransactionRequest;
import com.financetracker.finance_tracker.dto.TransactionResponse;
import com.financetracker.finance_tracker.entity.TransactionType;
import com.financetracker.finance_tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
private final TransactionService service;

@GetMapping
public ResponseEntity<List<TransactionResponse>> getAll
        (
         @RequestParam(required = false) TransactionType type,
         @RequestParam(required = false) Long categoryId
        )
{
    return ResponseEntity.ok(service.getAll(type, categoryId));
}

@GetMapping("/{id}")
public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
}

@PostMapping
public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest t) {
    return ResponseEntity.status(201).body(service.create(t));
}

@PutMapping("/{id}")
public ResponseEntity<TransactionResponse> update(@Valid @RequestBody TransactionRequest t, @PathVariable Long id) {
    return ResponseEntity.ok(service.update(id, t));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
}

@GetMapping("/summary")
public ResponseEntity<SummaryResponse> summary(){
    return ResponseEntity.ok(service.financeSummary());
}

}
