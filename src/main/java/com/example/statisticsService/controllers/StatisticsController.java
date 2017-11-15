package com.example.statisticsService.controllers;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class StatisticsController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/statistics")
    Statistics getStatistics() {
        return transactionService.getStatistics();
    }
}
