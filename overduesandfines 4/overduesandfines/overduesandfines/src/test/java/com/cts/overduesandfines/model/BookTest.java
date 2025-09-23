package com.cts.overduesandfines.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
    }

    @Test
    void testBookCreation() {
        book.setId(1L);
        book.setTitle("Java Programming");
        book.setAuthor("John Doe");

        assertEquals(1L, book.getId());
        assertEquals("Java Programming", book.getTitle());
        assertEquals("John Doe", book.getAuthor());
    }

    @Test
    void testBookSettersAndGetters() {
        book.setTitle("Spring Boot Guide");
        book.setAuthor("Jane Smith");

        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertEquals("Spring Boot Guide", book.getTitle());
        assertEquals("Jane Smith", book.getAuthor());
    }
}