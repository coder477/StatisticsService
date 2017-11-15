package com.example.statisticsService.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Transaction {
    private double amount;
    private Instant timestamp;

    public Transaction(double amount, Instant timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
