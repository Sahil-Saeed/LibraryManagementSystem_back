package com.cts.overduesandfines.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    private Long memberId;
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private FineStatus status = FineStatus.PENDING;
    
    private LocalDate transactionDate;

    public enum FineStatus {
        PAID, PENDING
    }
}
