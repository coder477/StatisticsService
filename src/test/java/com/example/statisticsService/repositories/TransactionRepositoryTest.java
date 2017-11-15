package com.example.statisticsService.repositories;

import com.example.statisticsService.models.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

import static java.time.Instant.now;
import static org.hamcrest.CoreMatchers.*;
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

    @Test
    public void shouldReturnTransactionsWithinConfiguredInterval() throws Exception {
        final Transaction transactionOne = new Transaction(1.2, now().minusSeconds(59));
        final Transaction transactionTwo = new Transaction(2.4, now().minusSeconds(60));
        transactionRepository.add(transactionOne);
        transactionRepository.add(transactionTwo);

        Thread.sleep(1000);
        final List<Transaction> transactions = transactionRepository.getTransactions();

        assertThat(transactions, is(notNullValue()));
        assertThat(transactions.size(), is(1));
        assertThat(transactions, hasItem(transactionOne));
    }

    @Test
    public void shouldRemoveTransactionsOlderThanConfiguredInterval() throws Exception {
        final Transaction transactionOne = new Transaction(1.2, now().minusSeconds(60));
        final Transaction transactionTwo = new Transaction(2.4, now().minusSeconds(59));
        final Boolean isFirstAdded = transactionRepository.add(transactionOne);
        final Boolean isSecondAdded = transactionRepository.add(transactionTwo);

        Thread.sleep(1000);
        transactionRepository.removeOld();

        assertThat(isFirstAdded, is(true));
        assertThat(isSecondAdded, is(true));
        ConcurrentNavigableMap<Long, List<Transaction>> map = getTransactionsMapUsingReflection();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), is(1));
        assertThat(map.containsKey(transactionTwo.getTimestamp().getEpochSecond()), is(true));
    }

    @SuppressWarnings("unchecked")
    private ConcurrentNavigableMap<Long, List<Transaction>> getTransactionsMapUsingReflection() {
        Field field = ReflectionUtils.findField(TransactionRepository.class, "transactions");
        if (field != null) {
            field.setAccessible(true);
            Object fieldValue = ReflectionUtils.getField(field, transactionRepository);
            return (ConcurrentNavigableMap<Long, List<Transaction>>) fieldValue;
        }
        return null;
    }
}