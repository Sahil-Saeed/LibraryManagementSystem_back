package com.cts.overduesandfines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBorrowingDetailsDto {
    private Long memberId;
    private List<BorrowingDto> borrowings;
    private List<FineDto> fines;
    private int totalBorrowedBooks;
    private int overdueBooks;
    private double totalFineAmount;
}