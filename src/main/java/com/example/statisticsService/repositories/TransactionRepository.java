package com.example.statisticsService.repositories;

import com.example.statisticsService.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.time.Instant.now;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
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
                .tailMap(getIntervalStartKey())
                .values()
                .parallelStream()
                .flatMap(Collection::parallelStream)
                .collect(toList());
    }

    private long getIntervalStartKey() {
        return now().minusSeconds(statisticsInterval).getEpochSecond();
    }

    @Scheduled(fixedDelay = 10 * 1000)
    void removeOld() {
        final int currentSize = transactions.size();
        log.info(String.format("Removing old entries if old entries > 0, currentSize=[%d]", currentSize));
        if (currentSize > 0) {
            final ConcurrentNavigableMap<Long, List<Transaction>> oldEntries = transactions.headMap(getIntervalStartKey());
            if (oldEntries.size() > 0) {
                log.info(String.format("removing [%s]", oldEntries));
                // Revisit to see if this has performance issues. (Maybe creating a new Map with tailMap?)
                oldEntries.clear();
                log.info("Updated size = " + transactions.size());
            }
        }
    }
}
