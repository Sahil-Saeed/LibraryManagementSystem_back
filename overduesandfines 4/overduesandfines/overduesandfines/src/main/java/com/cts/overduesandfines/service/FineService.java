package com.cts.overduesandfines.service;

import com.cts.overduesandfines.client.BorrowingReturningClient;
import com.cts.overduesandfines.dto.BorrowingDto;
import com.cts.overduesandfines.dto.FineDto;
import com.cts.overduesandfines.dto.MemberBorrowingDetailsDto;
import com.cts.overduesandfines.dto.MemberOverdueDetailsDto;
import com.cts.overduesandfines.exception.ResourceNotFoundException;
import com.cts.overduesandfines.model.Fine;
import com.cts.overduesandfines.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FineService {
    @Autowired
    private FineRepository fineRepository;
    
    @Autowired
    private BorrowingReturningClient borrowingClient;
    
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("2.00");

    public FineDto createFine(Long memberId, BigDecimal amount) {
        Fine fine = new Fine();
        fine.setMemberId(memberId);
        fine.setAmount(amount);
        fine.setStatus(Fine.FineStatus.PENDING);
        
        Fine savedFine = fineRepository.save(fine);
        return convertToDto(savedFine);
    }

    public FineDto payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
            .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        
        fine.setStatus(Fine.FineStatus.PAID);
        fine.setTransactionDate(LocalDate.now());
        
        Fine savedFine = fineRepository.save(fine);
        return convertToDto(savedFine);
    }

    public List<FineDto> getPendingFines() {
        return fineRepository.findByStatus(Fine.FineStatus.PENDING)
            .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<FineDto> getPaidFines() {
        return fineRepository.findByStatus(Fine.FineStatus.PAID)
            .stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public List<FineDto> getFinesByMemberId(Long memberId) {
        return fineRepository.findByMemberId(memberId)
            .stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public MemberOverdueDetailsDto getMemberOverdueAndFines(Long memberId) {
        System.out.println("=== AUTOMATIC OVERDUE & FINE CALCULATION FOR MEMBER: " + memberId + " ===");
        
        // 1. Get all borrowings for this member
        List<BorrowingDto> memberBorrowings = borrowingClient.getBorrowingsByMemberId(memberId.toString());
        System.out.println("Found " + memberBorrowings.size() + " total borrowings for member " + memberId);
        
        // 2. Filter overdue books
        List<BorrowingDto> overdueBooks = memberBorrowings.stream()
            .filter(this::isOverdue)
            .collect(Collectors.toList());
        System.out.println("Found " + overdueBooks.size() + " overdue books");
        
        // 3. Automatically calculate and create fines for overdue books
        List<FineDto> calculatedFines = new ArrayList<>();
        for (BorrowingDto overdueBorrowing : overdueBooks) {
            // Check if fine already exists for this borrowing
            boolean fineExists = fineRepository.findByMemberId(memberId).stream()
                .anyMatch(fine -> fine.getStatus() == Fine.FineStatus.PENDING);
            
            if (!fineExists) {
                System.out.println("Creating fine for overdue book ID: " + overdueBorrowing.getBookId());
                FineDto newFine = createFineFromBorrowing(overdueBorrowing);
                calculatedFines.add(newFine);
            }
        }
        
        // 4. Get all existing fines for this member
        List<FineDto> allMemberFines = getFinesByMemberId(memberId);
        calculatedFines.addAll(allMemberFines);
        
        // 5. Calculate total fine amount
        double totalFineAmount = calculatedFines.stream()
            .filter(fine -> "PENDING".equals(fine.getStatus()))
            .mapToDouble(fine -> fine.getAmount().doubleValue())
            .sum();
        
        // 6. Create response
        MemberOverdueDetailsDto response = new MemberOverdueDetailsDto();
        response.setMemberId(memberId);
        response.setOverdueBooks(overdueBooks);
        response.setCalculatedFines(calculatedFines);
        response.setTotalOverdueBooks(overdueBooks.size());
        response.setTotalFineAmount(totalFineAmount);
        
        System.out.println("=== CALCULATION COMPLETE: " + overdueBooks.size() + " overdue books, $" + totalFineAmount + " total fines ===");
        return response;
    }
    
    public MemberBorrowingDetailsDto getMemberBorrowingDetails(Long memberId) {
        System.out.println("=== FETCHING COMPLETE BORROWING DETAILS FOR MEMBER: " + memberId + " ===");
        
        // Get all borrowings for this member
        List<BorrowingDto> memberBorrowings = borrowingClient.getBorrowingsByMemberId(memberId.toString());
        System.out.println("Found " + memberBorrowings.size() + " total borrowings for member " + memberId);
        
        // Get all fines for this member
        List<FineDto> memberFines = getFinesByMemberId(memberId);
        System.out.println("Found " + memberFines.size() + " fines for member " + memberId);
        
        // Calculate statistics
        int totalBorrowed = memberBorrowings.size();
        int overdueCount = (int) memberBorrowings.stream()
            .filter(this::isOverdue)
            .count();
        
        double totalFineAmount = memberFines.stream()
            .filter(fine -> "PENDING".equals(fine.getStatus()))
            .mapToDouble(fine -> fine.getAmount().doubleValue())
            .sum();
        
        // Create response
        MemberBorrowingDetailsDto details = new MemberBorrowingDetailsDto();
        details.setMemberId(memberId);
        details.setBorrowings(memberBorrowings);
        details.setFines(memberFines);
        details.setTotalBorrowedBooks(totalBorrowed);
        details.setOverdueBooks(overdueCount);
        details.setTotalFineAmount(totalFineAmount);
        
        System.out.println("=== BORROWING DETAILS COMPLETE: " + totalBorrowed + " total books, " + overdueCount + " overdue, $" + totalFineAmount + " fines ===");
        return details;
    }
    
    public List<FineDto> processOverdueBorrowings() {
        System.out.println("Fetching overdue borrowings from borrowing service...");
        List<BorrowingDto> overdueBorrowings = borrowingClient.getOverdueBorrowings();
        System.out.println("Found " + overdueBorrowings.size() + " overdue borrowings");
        
        List<FineDto> createdFines = new ArrayList<>();
        for (BorrowingDto borrowing : overdueBorrowings) {
            if (isOverdue(borrowing)) {
                FineDto fine = createFineFromBorrowing(borrowing);
                createdFines.add(fine);
            }
        }
        return createdFines;
    }
    
    private boolean isOverdue(BorrowingDto borrowing) {
        return "Borrowed".equals(borrowing.getStatus()) && 
               borrowing.getBorrowDate() != null && 
               borrowing.getBorrowDate().isBefore(LocalDate.now().minusDays(14)); // 14 days loan period
    }
    
    private FineDto createFineFromBorrowing(BorrowingDto borrowing) {
        int overdueDays = calculateOverdueDays(borrowing);
        BigDecimal fineAmount = DAILY_FINE_RATE.multiply(new BigDecimal(overdueDays));
        
        Fine fine = new Fine();
        fine.setMemberId(borrowing.getMemberId());
        fine.setAmount(fineAmount);
        fine.setStatus(Fine.FineStatus.PENDING);
        
        Fine savedFine = fineRepository.save(fine);
        return convertToDto(savedFine);
    }
    
    private int calculateOverdueDays(BorrowingDto borrowing) {
        LocalDate dueDate = borrowing.getBorrowDate().plusDays(14); // 14 days loan period
        return (int) dueDate.until(LocalDate.now()).getDays();
    }
    
    private FineDto convertToDto(Fine fine) {
        FineDto dto = new FineDto();
        dto.setFineId(fine.getFineId());
        dto.setMemberId(fine.getMemberId());
        dto.setAmount(fine.getAmount());
        dto.setStatus(fine.getStatus().name());
        dto.setTransactionDate(fine.getTransactionDate());
        return dto;
    }
}

