package com.cts.overduesandfines;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cts.overduesandfines.controller.OverdueController;
import com.cts.overduesandfines.controller.FineController;
import com.cts.overduesandfines.service.OverdueService;
import com.cts.overduesandfines.service.FineService;
import com.cts.overduesandfines.exception.ResourceNotFoundException;
import com.cts.overduesandfines.model.Book;
import com.cts.overduesandfines.model.Fine;
import com.cts.overduesandfines.model.Member;
import com.cts.overduesandfines.model.Overdue;

@ExtendWith(MockitoExtension.class)
public class OverdueFineServiceTests {
    @Mock
    private OverdueService overdueService;
    @Mock
    private FineService fineService;

    @InjectMocks
    private OverdueController overdueController;
    @InjectMocks
    private FineController fineController;

    private Member member;
    private Book book;
    private Overdue overdue;
    private Fine fine;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setMemberId("M001");
        book = new Book();
        book.setId(1L);
        book.setTitle("Java 101");
        overdue = new Overdue();
        overdue.setId(1L);
        overdue.setMember(member);
        overdue.setBook(book);
        overdue.setDueDate(LocalDate.now().minusDays(5));
        overdue.setResolved(false);
        fine = new Fine();
        fine.setId(1L);
        fine.setOverdue(overdue);
        fine.setAmount(new BigDecimal("10.00"));
        fine.setPaid(false);
    }

    @Test
    void testOverdueNotFound() {
        when(overdueService.getOverdueById(99L)).thenThrow(new ResourceNotFoundException("Overdue not found"));
        assertThrows(ResourceNotFoundException.class, () -> overdueController.getOverdue(99L));
    }

    @Test
    void testFineNotFound() {
        when(fineService.getFineById(99L)).thenThrow(new ResourceNotFoundException("Fine not found"));
        assertThrows(ResourceNotFoundException.class, () -> fineController.getFine(99L));
    }
}
