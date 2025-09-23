package com.cts.overduesandfines.client;

import com.cts.overduesandfines.dto.BorrowingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "BORROW-RETURN")
public interface BorrowingReturningClient {
    
    @GetMapping("/api/borrowings/overdue")
    List<BorrowingDto> getOverdueBorrowings();
    
    @GetMapping("/api/borrowings/member/{memberId}")
    List<BorrowingDto> getBorrowingsByMemberId(@PathVariable("memberId") String memberId);
}