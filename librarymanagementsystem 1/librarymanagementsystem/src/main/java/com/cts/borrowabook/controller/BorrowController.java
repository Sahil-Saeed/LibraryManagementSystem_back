package com.cts.borrowabook.controller;

import com.cts.borrowabook.dto.BorrowReturnRequest;
import com.cts.borrowabook.entity.BorrowTransaction;
import com.cts.borrowabook.service.BorrowService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody BorrowReturnRequest request) {
        String result = borrowService.borrowBook(request.getMemberId(), request.getBookId());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/transactions/{id}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        String result = borrowService.returnBook(id);
        return ResponseEntity.ok(result);
    }

    // In BorrowController.java
    @GetMapping("/transactions")
    public ResponseEntity<List<BorrowTransaction>> getAllTransactions() {
        List<BorrowTransaction> transactions = borrowService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/borrowings/{id}")
    public ResponseEntity<BorrowTransaction> getBorrowingById(@PathVariable Long id) {
        BorrowTransaction borrowing = borrowService.getBorrowingById(id);
        return ResponseEntity.ok(borrowing);
    }

    @GetMapping("/borrowings/member/{memberId}")
    public ResponseEntity<List<BorrowTransaction>> getBorrowingsByMemberId(@PathVariable String memberId) {
        List<BorrowTransaction> borrowings = borrowService.getBorrowingsByMemberId(memberId);
        return ResponseEntity.ok(borrowings);
    }

    @GetMapping("/borrowings/overdue")
    public ResponseEntity<List<BorrowTransaction>> getOverdueBorrowings() {
        List<BorrowTransaction> overdueBorrowings = borrowService.getOverdueBorrowings();
        return ResponseEntity.ok(overdueBorrowings);
    }

    @PostMapping("/returns/{borrowingId}")
    public ResponseEntity<BorrowTransaction> returnBookById(@PathVariable Long borrowingId) {
        BorrowTransaction result = borrowService.returnBookById(borrowingId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/returns/with-fine")
    public ResponseEntity<BorrowTransaction> returnBookWithFine(@RequestBody BorrowTransaction borrowingDto) {
        BorrowTransaction result = borrowService.returnBookWithFine(borrowingDto);
        return ResponseEntity.ok(result);
    }
}
