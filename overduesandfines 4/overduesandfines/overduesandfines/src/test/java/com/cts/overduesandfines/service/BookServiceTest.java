package com.cts.overduesandfines.service;

import com.cts.overduesandfines.client.BookClient;
import com.cts.overduesandfines.dto.BookDto;
import com.cts.overduesandfines.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Java Programming");
        book.setAuthor("John Doe");
        
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Java Programming");
        bookDto.setAuthor("John Doe");
    }

    @Test
    void getBookById_Success() {
        when(bookClient.getBookById(1L)).thenReturn(bookDto);

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java Programming", result.getTitle());
        assertEquals("John Doe", result.getAuthor());
        verify(bookClient).getBookById(1L);
    }

    @Test
    void getBookById_NotFound_ReturnsNull() {
        when(bookClient.getBookById(99L)).thenReturn(null);

        Book result = bookService.getBookById(99L);

        assertNull(result);
        verify(bookClient).getBookById(99L);
    }
}