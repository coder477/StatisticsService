package com.example.statisticsService.services;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.models.Transaction;
import com.example.statisticsService.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;

import static java.util.stream.Collectors.summarizingDouble;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Boolean add(Transaction transaction) {
        return transactionRepository.add(transaction);
    }

    public Statistics getStatistics() {
        final DoubleSummaryStatistics stats = transactionRepository.getTransactions()
                .parallelStream()
                .collect(summarizingDouble(Transaction::getAmount));
        return new Statistics(stats.getSum(), stats.getMax(), stats.getMin(), stats.getCount(), stats.getAverage());
    }
}
