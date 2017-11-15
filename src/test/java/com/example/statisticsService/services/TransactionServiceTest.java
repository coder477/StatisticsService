package com.example.statisticsService.services;

import com.example.statisticsService.models.Transaction;
import com.example.statisticsService.repositories.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.time.Instant.now;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void shouldAddTransactionToCache() throws Exception {
        final Transaction transaction = new Transaction(5.3, now());
        when(transactionRepository.add(transaction)).thenReturn(true);

        final Boolean isAdded = transactionService.add(transaction);

        verify(transactionRepository).add(transaction);
        assertThat(isAdded, is(true));
    }
}