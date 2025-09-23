package com.cts.overduesandfines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineDto {
    private Long fineId;
    private Long memberId;
    private BigDecimal amount;
    private String status; // Paid, Pending
    private LocalDate transactionDate;
}