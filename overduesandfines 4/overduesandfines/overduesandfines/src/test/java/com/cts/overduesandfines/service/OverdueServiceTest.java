package com.cts.overduesandfines.service;

import com.cts.overduesandfines.exception.ResourceNotFoundException;
import com.cts.overduesandfines.model.Book;
import com.cts.overduesandfines.model.Member;
import com.cts.overduesandfines.model.Overdue;
import com.cts.overduesandfines.repository.BookRepository;
import com.cts.overduesandfines.repository.MemberRepository;
import com.cts.overduesandfines.repository.OverdueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverdueServiceTest {

    @Mock
    private OverdueRepository overdueRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OverdueService overdueService;

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
        book.setAuthor("Author");

        overdue = new Overdue();
        overdue.setId(1L);
        overdue.setMember(member);
        overdue.setBook(book);
        overdue.setDueDate(LocalDate.now().minusDays(5));
        overdue.setResolved(false);
    }

    @Test
    void getOverdueById_Success() {
        when(overdueRepository.findById(1L)).thenReturn(Optional.of(overdue));

        Overdue result = overdueService.getOverdueById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(overdueRepository).findById(1L);
    }

    @Test
    void getOverdueById_NotFound_ThrowsException() {
        when(overdueRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> overdueService.getOverdueById(99L)
        );

        assertEquals("Overdue not found", exception.getMessage());
    }

    @Test
    void createOverdue_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(overdueRepository.save(overdue)).thenReturn(overdue);

        Overdue result = overdueService.createOverdue(overdue);

        assertNotNull(result);
        verify(overdueRepository).save(overdue);
    }

    @Test
    void createOverdue_NullMember_ThrowsException() {
        overdue.setMember(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> overdueService.createOverdue(overdue)
        );

        assertEquals("Member is required", exception.getMessage());
    }

    @Test
    void createOverdue_NullBook_ThrowsException() {
        overdue.setBook(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> overdueService.createOverdue(overdue)
        );

        assertEquals("Book is required", exception.getMessage());
    }

    @Test
    void createOverdue_NullDueDate_ThrowsException() {
        overdue.setDueDate(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> overdueService.createOverdue(overdue)
        );

        assertEquals("dueDate is required", exception.getMessage());
    }

    @Test
    void getFilteredOverdues_WithMemberName() {
        List<Overdue> overdues = Arrays.asList(overdue);
        when(overdueRepository.findAll()).thenReturn(overdues);

        List<Overdue> result = overdueService.getFilteredOverdues(0, 10, "John", null, null);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getMember().getName());
    }

    @Test
    void getOverduesByMemberId_Success() {
        List<Overdue> overdues = Arrays.asList(overdue);
        when(overdueRepository.findByMember_MemberId("M001")).thenReturn(overdues);

        List<Overdue> result = overdueService.getOverduesByMemberId("M001");

        assertEquals(1, result.size());
        verify(overdueRepository).findByMember_MemberId("M001");
    }
}