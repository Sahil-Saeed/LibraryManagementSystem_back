package com.cts.borrowabook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowReturnRequest {
    private Long memberId;
    private int bookId;
}
