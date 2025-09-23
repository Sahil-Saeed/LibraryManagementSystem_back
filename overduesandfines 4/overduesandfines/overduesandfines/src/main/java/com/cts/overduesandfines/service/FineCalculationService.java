package com.cts.overduesandfines.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class FineCalculationService {
    
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("2.00");
    
    public BigDecimal calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }
        
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return DAILY_FINE_RATE.multiply(new BigDecimal(overdueDays));
    }
}