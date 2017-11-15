package com.example.statisticsService.services;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.models.Transaction;
import com.example.statisticsService.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
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
        return new Statistics(stats.getSum(), getMax(stats), getMin(stats), stats.getCount(), stats.getAverage());
    }

    private double getMax(DoubleSummaryStatistics stats) {
        final double max = stats.getMax();
        return NEGATIVE_INFINITY == max ? 0 : max;
    }

    private double getMin(DoubleSummaryStatistics stats) {
        final double min = stats.getMin();
        return POSITIVE_INFINITY == min ? 0 : min;
    }
}
