package com.example.statisticsService.services;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.models.Transaction;
import com.example.statisticsService.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Boolean add(Transaction transaction) {
        return transactionRepository.add(transaction);
    }

    public Statistics getStatistics() {
        return null;
    }
}
