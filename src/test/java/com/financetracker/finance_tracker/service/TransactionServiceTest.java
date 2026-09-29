package com.financetracker.finance_tracker.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionRepository transactionRepository;



    @Test
    void getById_shouldReturnTransaction_whenTrasactionExists(){
        Transaction transaction = new Transaction();
        Category category = new Category();

        category.setId(1L);
        category.setName("Еда");

        transaction.setId(1L);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCategory(category);
        transaction.setDescription("smth");
        transaction.setDate(LocalDate.now());
        transaction.setType(TransactionType.EXPENSE);

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponse transactionResponse = transactionService.getById(1L);

        Assertions.assertEquals(1L, transactionResponse.getId());
        Assertions.assertEquals("Еда", transactionResponse.getCategoryName());


    }

    @Test
    void getById_shoudThrowResourseNotFoundException_whenTransactionGetByIdIsNull(){

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {transactionService.getById(1L);});
    }

    @Test
    void getSummary_shouldReturnZero_WhenSetAmountByTypeIsZero(){

        Mockito.when(transactionRepository.sumAmountByType(TransactionType.EXPENSE)).thenReturn(null);

        Assertions.assertEquals(BigDecimal.ZERO, transactionService.getSummary(TransactionType.EXPENSE));
    }

}
