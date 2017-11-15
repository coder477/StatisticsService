package com.example.statisticsService.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Statistics {
    private double sum;
    private double max;
    private double min;
    private int count;

    public Statistics(double sum, double max, double min, int count) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Double getAverage() {
        return sum / count;
    }
}
