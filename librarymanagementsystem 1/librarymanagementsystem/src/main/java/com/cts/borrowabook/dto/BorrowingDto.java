package com.cts.borrowabook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingDto {
    private Long id;
    private int bookId;
    private Long memberId;
    private Instant borrowDate;
    private Instant returnDate;
    private String status;
    private Double fine;
}