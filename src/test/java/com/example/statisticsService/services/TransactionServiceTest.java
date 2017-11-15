package com.example.statisticsService.services;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.models.Transaction;
import com.example.statisticsService.repositories.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
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

    @Test
    public void shouldReturnStatisticsFromTransactions() throws Exception {
        final Transaction transactionOne = new Transaction(4, now());
        final Transaction transactionTwo = new Transaction(8, now());
        when(transactionRepository.getTransactions()).thenReturn(asList(transactionOne, transactionTwo));
        final Statistics expectedStatistic = new Statistics(12, 8, 4, 2, 6);

        final Statistics actualStatistic = transactionService.getStatistics();

        verify(transactionRepository).getTransactions();
        assertThat(actualStatistic, is(expectedStatistic));

    }
}