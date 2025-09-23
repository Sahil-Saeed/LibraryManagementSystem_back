package com.cts.borrowabook.service;

import com.cts.borrowabook.client.BookServiceClient;
import com.cts.borrowabook.client.MemberServiceClient;
import com.cts.borrowabook.dto.BookDto;
import com.cts.borrowabook.dto.MemberDto;
import com.cts.borrowabook.entity.BorrowTransaction;
import com.cts.borrowabook.exception.BookNotFoundException;
import com.cts.borrowabook.exception.BookUnavailableException;
import com.cts.borrowabook.exception.MemberNotFoundException;
import com.cts.borrowabook.repository.BorrowTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class BorrowService {

    private final MemberServiceClient memberServiceClient;
    private final BookServiceClient bookServiceClient;
    private final BorrowTransactionRepository borrowTransactionRepository;

    @Transactional
    public String borrowBook(Long memberId, int bookId) {
        try {
            // Verify member exists
            MemberDto member = memberServiceClient.getMemberById(memberId);
            if (member == null) {
                throw new MemberNotFoundException("Member not found with id: " + memberId);
            }
        } catch (Exception e) {
            throw new MemberNotFoundException("Member not found with id: " + memberId);
        }

        // Check borrowing limits
        if (!checkBorrowingLimit(memberId)) {
            throw new RuntimeException("Member has reached borrowing limit");
        }

        try {
            // Verify book exists and is available
            BookDto book = bookServiceClient.getBookById(bookId);
            if (book == null) {
                throw new BookNotFoundException("Book not found with id: " + bookId);
            }
            if (book.getAvailableCopies() <= 0) {
                throw new BookUnavailableException("Book with ID " + bookId + " is not available for borrowing.");
            }
        } catch (BookNotFoundException | BookUnavailableException e) {
            throw e;
        } catch (Exception e) {
            throw new BookNotFoundException("Book not found with id: " + bookId);
        }

        BorrowTransaction transaction = new BorrowTransaction();
        transaction.setMemberId(memberId);
        transaction.setBookId(bookId);
        transaction.setBorrowDate(Instant.now());
        transaction.setStatus(BorrowTransaction.Status.BORROWED);
        borrowTransactionRepository.save(transaction);

        // Decrease available copies by 1
        bookServiceClient.updateAvailableCopies(bookId, -1);

        return "Book borrowed successfully.";
    }

    @Transactional
    public String returnBook(Long transactionId) {
        BorrowTransaction transaction = borrowTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));

        if (transaction.getStatus() == BorrowTransaction.Status.RETURNED) {
            return "Book is already returned.";
        }

        Instant returnedDate = Instant.now();
        transaction.setReturnDate(returnedDate);
        transaction.setStatus(BorrowTransaction.Status.RETURNED);

        long daysBetween = ChronoUnit.DAYS.between(transaction.getBorrowDate(), returnedDate);
        long fineDays = daysBetween - 14;

        if (fineDays > 0) {
            transaction.setFine(fineDays * 0.5);
        } else {
            transaction.setFine(0.0);
        }

        borrowTransactionRepository.save(transaction);

        // Increase available copies by 1
        bookServiceClient.updateAvailableCopies(transaction.getBookId(), 1);

        if (transaction.getFine() > 0) {
            return "Book returned successfully. Fine applied: $" + transaction.getFine();
        } else {
            return "Book returned successfully. No fine applied.";
        }
    }


    public List<BorrowTransaction> getAllTransactions() {
        return borrowTransactionRepository.findAll();
    }

    private boolean checkBorrowingLimit(Long memberId) {
        long activeBorrows = borrowTransactionRepository.countByMemberIdAndStatus(memberId, BorrowTransaction.Status.BORROWED);
        return activeBorrows < 3; // Maximum 3 books per member
    }

    public BorrowTransaction getBorrowingById(Long id) {
        return borrowTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + id));
    }

    public List<BorrowTransaction> getBorrowingsByMemberId(String memberId) {
        return borrowTransactionRepository.findByMemberId(Long.valueOf(memberId));
    }

    public List<BorrowTransaction> getOverdueBorrowings() {
        Instant fourteenDaysAgo = Instant.now().minus(14, ChronoUnit.DAYS);
        return borrowTransactionRepository.findOverdueBorrowings(fourteenDaysAgo);
    }

    @Transactional
    public BorrowTransaction returnBookById(Long borrowingId) {
        BorrowTransaction transaction = borrowTransactionRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + borrowingId));

        if (transaction.getStatus() == BorrowTransaction.Status.RETURNED) {
            throw new RuntimeException("Book is already returned.");
        }

        Instant returnedDate = Instant.now();
        transaction.setReturnDate(returnedDate);
        transaction.setStatus(BorrowTransaction.Status.RETURNED);

        long daysBetween = ChronoUnit.DAYS.between(transaction.getBorrowDate(), returnedDate);
        long fineDays = daysBetween - 14;

        if (fineDays > 0) {
            transaction.setFine(fineDays * 0.5);
        } else {
            transaction.setFine(0.0);
        }

        borrowTransactionRepository.save(transaction);
        bookServiceClient.updateAvailableCopies(transaction.getBookId(), 1);

        return transaction;
    }

    @Transactional
    public BorrowTransaction returnBookWithFine(BorrowTransaction borrowingDto) {
        BorrowTransaction transaction = borrowTransactionRepository.findById(borrowingDto.getId())
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + borrowingDto.getId()));

        transaction.setReturnDate(Instant.now());
        transaction.setStatus(BorrowTransaction.Status.RETURNED);
        transaction.setFine(borrowingDto.getFine());

        borrowTransactionRepository.save(transaction);
        bookServiceClient.updateAvailableCopies(transaction.getBookId(), 1);

        return transaction;
    }
}
