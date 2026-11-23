package com.financetracker.finance_tracker.service;

import com.financetracker.finance_tracker.dto.TransactionRequest;
import com.financetracker.finance_tracker.dto.TransactionResponse;
import com.financetracker.finance_tracker.entity.Category;
import com.financetracker.finance_tracker.entity.Transaction;
import com.financetracker.finance_tracker.entity.TransactionType;
import com.financetracker.finance_tracker.exception.ResourceNotFoundException;
import com.financetracker.finance_tracker.repository.CategoryRepository;
import com.financetracker.finance_tracker.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.financetracker.finance_tracker.entity.TransactionType.EXPENSE;
import static com.financetracker.finance_tracker.entity.TransactionType.INCOME;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionRepository transactionRepository;


    @Test
    void getById_shouldReturnTransaction_whenTransactionExists() {
        Transaction transaction = new Transaction();
        Category category = new Category();

        ReflectionTestUtils.setField(category, "id", 1L);
        category.setName("Еда");

        ReflectionTestUtils.setField(transaction, "id", 1L);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCategory(category);
        transaction.setDescription("smth");
        transaction.setDate(LocalDate.now());
        transaction.setType(EXPENSE);

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponse transactionResponse = transactionService.getById(1L);

        Assertions.assertEquals(1L, transactionResponse.getId());
        Assertions.assertEquals("Еда", transactionResponse.getCategoryName());


    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenTransactionGetByIdIsNull() {

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getById(1L);
        });
    }

    @Test
    void getSummary_shouldReturnZero_WhenSetAmountByTypeIsNull() {

        Mockito.when(transactionRepository.sumAmountByType(EXPENSE)).thenReturn(null);

        Assertions.assertEquals(BigDecimal.ZERO, transactionService.getSummary(EXPENSE));
    }

    @Test
    void delete_shouldDeleteTransaction_whenItExists() {
        Transaction transaction = new Transaction();
        Category category = new Category();

        ReflectionTestUtils.setField(category, "id", 1L);
        category.setName("Еда");

        ReflectionTestUtils.setField(transaction, "id", 1L);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCategory(category);
        transaction.setDescription("smth");
        transaction.setDate(LocalDate.now());
        transaction.setType(EXPENSE);

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.delete(1L);

        Mockito.verify(transactionRepository, Mockito.times(1)).delete(transaction);


    }

    @Test
    void delete_shouldThrowRNFE_whenTransactionNotFound() {

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.delete(1L);
        });

        Mockito.verify(transactionRepository, Mockito.never()).delete(any(Transaction.class));

    }

    @Test
    void getAll_shouldUseBothFilters_whenTypeAndCategoryGiven() {
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(transactionRepository.findByTypeAndCategoryId(EXPENSE, 1L, pageable))
            .thenReturn(Page.empty());

        transactionService.getAll(pageable, EXPENSE, 1L);

        Mockito.verify(transactionRepository, Mockito.times(1)).findByTypeAndCategoryId(EXPENSE, 1L, pageable);
        Mockito.verify(transactionRepository, Mockito.never()).findByType(any(), any());
        Mockito.verify(transactionRepository, Mockito.never()).findByCategoryId(any(), any());
        Mockito.verify(transactionRepository, Mockito.never()).findAll(any(Pageable.class));
    }

    @Test
    void update_shouldUpdateAndSave_whenTransactionAndCategoryExists() {
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        Category existingCategory = new Category();
        Category newCategory = new Category();
        Transaction existingTransaction = new Transaction();
        TransactionRequest request = new TransactionRequest();

        ReflectionTestUtils.setField(existingCategory, "id", 1L);
        ReflectionTestUtils.setField(existingCategory, "name", "Шестёрочка");

        ReflectionTestUtils.setField(newCategory, "id", 2L);
        ReflectionTestUtils.setField(newCategory, "name", "Магнит");

        ReflectionTestUtils.setField(existingTransaction, "id", 1L);
        ReflectionTestUtils.setField(existingTransaction, "amount", new BigDecimal("1488.00"));
        ReflectionTestUtils.setField(existingTransaction, "category", existingCategory);
        ReflectionTestUtils.setField(existingTransaction, "description", "Оплата товаров и услуг");
        ReflectionTestUtils.setField(existingTransaction, "date", LocalDate.of(2026, 11, 20));
        ReflectionTestUtils.setField(existingTransaction, "type", EXPENSE);

        ReflectionTestUtils.setField(request, "amount", new BigDecimal("670.00"));
        ReflectionTestUtils.setField(request, "categoryId", newCategory.getId());
        ReflectionTestUtils.setField(request, "description", "Ежемесячная зарплата");
        ReflectionTestUtils.setField(request, "date", LocalDate.of(2026, 11, 21));
        ReflectionTestUtils.setField(request, "type", INCOME);

        Mockito.when(transactionRepository.findById(existingTransaction.getId())).thenReturn(Optional.of(existingTransaction));
        Mockito.when(categoryRepository.findById(newCategory.getId())).thenReturn(Optional.of(newCategory));
        Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(existingTransaction);

        TransactionResponse response = transactionService.update(existingTransaction.getId(), request);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals(new BigDecimal("670.00"), response.getAmount());
        Assertions.assertEquals("Магнит", response.getCategoryName());
        Assertions.assertEquals(2L, response.getCategoryId());
        Assertions.assertEquals("Ежемесячная зарплата", response.getDescription());
        Assertions.assertEquals(LocalDate.of(2026, 11, 21), response.getDate());
        Assertions.assertEquals(INCOME, response.getType());

        Mockito.verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        Assertions.assertEquals(1L, capturedTransaction.getId());
        Assertions.assertEquals(new BigDecimal("670.00"), capturedTransaction.getAmount());
        Assertions.assertEquals("Магнит", capturedTransaction.getCategory().getName());
        Assertions.assertEquals(2L, capturedTransaction.getCategory().getId());
        Assertions.assertEquals("Ежемесячная зарплата", capturedTransaction.getDescription());
        Assertions.assertEquals(LocalDate.of(2026, 11, 21), capturedTransaction.getDate());
        Assertions.assertEquals(INCOME, capturedTransaction.getType());
    }

    @Test
    void update_shouldThrowException_whenTransactionExistsAndCategoryNotExist() {
        Transaction existingTransaction = new Transaction();
        TransactionRequest request = new TransactionRequest();

        ReflectionTestUtils.setField(existingTransaction, "id", 1L);
        ReflectionTestUtils.setField(existingTransaction, "amount", new BigDecimal("1488.00"));
        ReflectionTestUtils.setField(existingTransaction, "description", "Оплата товаров и услуг");
        ReflectionTestUtils.setField(existingTransaction, "date", LocalDate.of(2026, 11, 20));
        ReflectionTestUtils.setField(existingTransaction, "type", EXPENSE);

        ReflectionTestUtils.setField(request, "amount", new BigDecimal("670.00"));
        ReflectionTestUtils.setField(request, "categoryId", 2L);
        ReflectionTestUtils.setField(request, "description", "Ежемесячная зарплата");
        ReflectionTestUtils.setField(request, "date", LocalDate.of(2026, 11, 21));
        ReflectionTestUtils.setField(request, "type", INCOME);

        Mockito.when(transactionRepository.findById(existingTransaction.getId())).thenReturn(Optional.of(existingTransaction));
        Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.update(existingTransaction.getId(), request);
        });

        Mockito.verify(transactionRepository, Mockito.never()).save(any(Transaction.class));
    }

    @Test
    void update_shouldThrowException_wheCategoryExistsAndTransactionNotExist() {
        TransactionRequest request = new TransactionRequest();

        ReflectionTestUtils.setField(request, "amount", new BigDecimal("670.00"));
        ReflectionTestUtils.setField(request, "categoryId", 2L);
        ReflectionTestUtils.setField(request, "description", "Ежемесячная зарплата");
        ReflectionTestUtils.setField(request, "date", LocalDate.of(2026, 11, 21));
        ReflectionTestUtils.setField(request, "type", INCOME);

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.update(1L, request);
        });

        Mockito.verify(transactionRepository, Mockito.never()).save(any(Transaction.class));
    }
}
