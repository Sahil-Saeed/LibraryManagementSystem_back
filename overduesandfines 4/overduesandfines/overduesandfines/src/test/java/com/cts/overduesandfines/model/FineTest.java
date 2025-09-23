package com.cts.overduesandfines.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FineTest {

    private Fine fine;
    private Overdue overdue;

    @BeforeEach
    void setUp() {
        fine = new Fine();
        overdue = new Overdue();
        overdue.setId(1L);
    }

    @Test
    void testFineCreation() {
        BigDecimal amount = new BigDecimal("10.00");
        LocalDate issuedDate = LocalDate.now();
        
        fine.setId(1L);
        fine.setOverdue(overdue);
        fine.setAmount(amount);
        fine.setIssuedDate(issuedDate);
        fine.setPaid(false);

        assertEquals(1L, fine.getId());
        assertEquals(overdue, fine.getOverdue());
        assertEquals(amount, fine.getAmount());
        assertEquals(issuedDate, fine.getIssuedDate());
        assertFalse(fine.isPaid());
    }

    @Test
    void testFinePayment() {
        fine.setPaid(true);
        assertTrue(fine.isPaid());
    }

    @Test
    void testFineAmount() {
        BigDecimal amount = new BigDecimal("25.50");
        fine.setAmount(amount);
        assertEquals(amount, fine.getAmount());
    }
}