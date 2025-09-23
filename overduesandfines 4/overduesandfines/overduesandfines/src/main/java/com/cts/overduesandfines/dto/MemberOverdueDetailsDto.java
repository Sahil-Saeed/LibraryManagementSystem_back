package com.cts.overduesandfines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberOverdueDetailsDto {
    private Long memberId;
    private List<BorrowingDto> overdueBooks;
    private List<FineDto> calculatedFines;
    private double totalFineAmount;
    private int totalOverdueBooks;
}