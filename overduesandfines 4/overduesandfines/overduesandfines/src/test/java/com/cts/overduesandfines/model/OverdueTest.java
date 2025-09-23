package com.cts.overduesandfines.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OverdueTest {

    private Overdue overdue;
    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        overdue = new Overdue();
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        
        book = new Book();
        book.setId(1L);
        book.setTitle("Java Programming");
    }

    @Test
    void testOverdueCreation() {
        LocalDate dueDate = LocalDate.now().minusDays(5);
        
        overdue.setId(1L);
        overdue.setMember(member);
        overdue.setBook(book);
        overdue.setDueDate(dueDate);
        overdue.setResolved(false);

        assertEquals(1L, overdue.getId());
        assertEquals(member, overdue.getMember());
        assertEquals(book, overdue.getBook());
        assertEquals(dueDate, overdue.getDueDate());
        assertFalse(overdue.isResolved());
    }

    @Test
    void testOverdueResolution() {
        overdue.setResolved(true);
        assertTrue(overdue.isResolved());
    }
}