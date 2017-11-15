package com.example.statisticsService.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Statistics {
    private double sum;
    private double max;
    private double min;
    private long count;
    private double average;

    public Statistics(double sum, double max, double min, long count, double average) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
        this.average = average;
    }
}
