package com.bezkoder.spring.jpa.h2.service.impl;

import com.bezkoder.spring.jpa.h2.business.Transaction;
import com.bezkoder.spring.jpa.h2.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class ImportationServiceImplTest {

    @InjectMocks
    private ImportationServiceImpl importationService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(importationService, "csvFilePath", "app/doc/full.csv");
    }

    @Test
    void importCsvData() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.saveAll(transactions)).thenReturn(transactions);

        importationService.importCsvData();

        verify(transactionRepository, times(1)).saveAll(transactions);
    }

    @Test
    void importCsvData_success() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());

        when(transactionRepository.saveAll(transactions)).thenReturn(transactions);

        importationService.importCsvData();

        verify(transactionRepository, times(1)).saveAll(transactions);
    }

    @Test
    void importCsvData_emptyFile() {
        List<Transaction> transactions = Collections.emptyList();

        importationService.importCsvData();

        verify(transactionRepository, never()).saveAll(transactions);
    }
}