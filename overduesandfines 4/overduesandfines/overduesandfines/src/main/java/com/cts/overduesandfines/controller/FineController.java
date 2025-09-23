package com.cts.overduesandfines.controller;

import com.cts.overduesandfines.dto.FineDto;
import com.cts.overduesandfines.dto.MemberBorrowingDetailsDto;
import com.cts.overduesandfines.dto.MemberOverdueDetailsDto;
import com.cts.overduesandfines.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {
    @Autowired
    private FineService fineService;

    // Main endpoint - Member clicks "Overdue & Fines" button
    @GetMapping("/member/{memberId}")
    public MemberOverdueDetailsDto getMemberOverdueAndFines(@PathVariable Long memberId) {
        return fineService.getMemberOverdueAndFines(memberId);
    }

    // Member borrowing history with all details
    @GetMapping("/member/{memberId}/borrowing-details")
    public MemberBorrowingDetailsDto getMemberBorrowingDetails(@PathVariable Long memberId) {
        return fineService.getMemberBorrowingDetails(memberId);
    }

    // Pay fine endpoint
    @PostMapping("/pay/{fineId}")
    public FineDto payFine(@PathVariable Long fineId) {
        return fineService.payFine(fineId);
    }
}