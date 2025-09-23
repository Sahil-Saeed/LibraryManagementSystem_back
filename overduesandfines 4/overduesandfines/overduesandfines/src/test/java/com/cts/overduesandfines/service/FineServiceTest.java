package com.cts.overduesandfines.service;

import com.cts.overduesandfines.exception.ResourceNotFoundException;
import com.cts.overduesandfines.model.Book;
import com.cts.overduesandfines.model.Fine;
import com.cts.overduesandfines.model.Member;
import com.cts.overduesandfines.model.Overdue;
import com.cts.overduesandfines.repository.FineRepository;
import com.cts.overduesandfines.repository.OverdueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineServiceTest {

    @Mock
    private FineRepository fineRepository;
    @Mock
    private OverdueRepository overdueRepository;

    @InjectMocks
    private FineService fineService;

    private Fine fine;
    private Overdue overdue;
    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setMemberId("M001");

        book = new Book();
        book.setId(1L);
        book.setTitle("Java Programming");

        overdue = new Overdue();
        overdue.setId(1L);
        overdue.setMember(member);
        overdue.setBook(book);
        overdue.setDueDate(LocalDate.now().minusDays(5));

        fine = new Fine();
        fine.setId(1L);
        fine.setOverdue(overdue);
        fine.setAmount(new BigDecimal("10.00"));
        fine.setIssuedDate(LocalDate.now());
        fine.setPaid(false);
    }

    @Test
    void getFineById_Success() {
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));

        Fine result = fineService.getFineById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("10.00"), result.getAmount());
        verify(fineRepository).findById(1L);
    }

    @Test
    void getFineById_NotFound_ThrowsException() {
        when(fineRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> fineService.getFineById(99L)
        );

        assertEquals("Fine not found", exception.getMessage());
    }

    @Test
    void createFine_Success() {
        when(overdueRepository.findById(1L)).thenReturn(Optional.of(overdue));
        when(fineRepository.save(fine)).thenReturn(fine);

        Fine result = fineService.createFine(fine);

        assertNotNull(result);
        verify(fineRepository).save(fine);
    }

    @Test
    void createFine_NullOverdue_ThrowsException() {
        fine.setOverdue(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> fineService.createFine(fine)
        );

        assertEquals("Overdue is required", exception.getMessage());
    }

    @Test
    void createFine_NullAmount_ThrowsException() {
        fine.setAmount(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> fineService.createFine(fine)
        );

        assertEquals("amount is required", exception.getMessage());
    }

    @Test
    void createFine_NullIssuedDate_ThrowsException() {
        fine.setIssuedDate(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> fineService.createFine(fine)
        );

        assertEquals("issuedDate is required", exception.getMessage());
    }

    @Test
    void getFilteredFines_WithMemberName() {
        List<Fine> fines = Arrays.asList(fine);
        when(fineRepository.findAll()).thenReturn(fines);

        List<Fine> result = fineService.getFilteredFines(0, 10, "John", null, null);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getOverdue().getMember().getName());
    }

    @Test
    void getFinesByMemberId_Success() {
        List<Fine> fines = Arrays.asList(fine);
        when(fineRepository.findByOverdue_Member_MemberId("M001")).thenReturn(fines);

        List<Fine> result = fineService.getFinesByMemberId("M001");

        assertEquals(1, result.size());
        verify(fineRepository).findByOverdue_Member_MemberId("M001");
    }

    @Test
    void getMemberData_Success() {
        List<Fine> fines = Arrays.asList(fine);
        when(fineRepository.findByOverdue_Member_MemberId("M001")).thenReturn(fines);

        Map<String, Object> result = fineService.getMemberData("M001");

        assertNotNull(result);
        assertEquals(fines, result.get("fines"));
    }
}