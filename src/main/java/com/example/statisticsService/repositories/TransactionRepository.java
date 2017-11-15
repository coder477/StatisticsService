package com.example.statisticsService.repositories;

import com.example.statisticsService.models.Transaction;

import java.time.Duration;

import static java.time.Instant.now;

public class TransactionRepository {

    private Long statisticsInterval = 60L;

    public Boolean add(Transaction transaction) {
        return Duration.between(transaction.getTimestamp(), now()).getSeconds() < statisticsInterval;
    }
}
