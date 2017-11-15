package com.example.statisticsService.repositories;

import com.example.statisticsService.models.Transaction;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import static java.time.Instant.now;
import static java.util.Objects.isNull;

@Component
public class TransactionRepository {
    private Long statisticsInterval = 60L;

    private ConcurrentNavigableMap<Long, List<Transaction>> transactions = new ConcurrentSkipListMap<>();

    public Boolean add(Transaction transaction) {
        if (isInvalidInterval(transaction)) {
            addToTransactionsBySecond(transaction);
            return true;
        }
        return false;
    }

    private boolean isInvalidInterval(Transaction transaction) {
        return Duration.between(transaction.getTimestamp(), now()).getSeconds() <= statisticsInterval;
    }

    private void addToTransactionsBySecond(Transaction transaction) {
        final long transactionSecond = transaction.getTimestamp().getEpochSecond();
        List<Transaction> transactionsAtSecond = transactions.get(transactionSecond);
        if (isNull(transactionsAtSecond)) {
            transactionsAtSecond = new ArrayList<>();
        }
        transactionsAtSecond.add(transaction);
        transactions.put(transactionSecond, transactionsAtSecond);
    }

    public List<Transaction> getTransactions() {
        return transactions
                .tailMap(now().minusSeconds(statisticsInterval).getEpochSecond())
                .values()
                .parallelStream()
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList());
    }
}
