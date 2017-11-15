package com.example.statisticsService.repositories;

import com.example.statisticsService.models.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static java.time.Instant.now;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRepositoryTest {
    @InjectMocks
    private TransactionRepository transactionRepository;

    @Test
    public void shouldSaveTransactionIfItLiesInConfiguredInterval() throws Exception {
        final Transaction transaction = new Transaction(12.3, now());

        final Boolean isAdded = transactionRepository.add(transaction);

        assertThat(isAdded, is(true));
    }

    @Test
    public void shouldNotSaveTransactionIfItLiesInConfiguredInterval() throws Exception {
        final Transaction transaction = new Transaction(12.3, now().minusSeconds(61));

        final Boolean isAdded = transactionRepository.add(transaction);

        assertThat(isAdded, is(false));
    }
}