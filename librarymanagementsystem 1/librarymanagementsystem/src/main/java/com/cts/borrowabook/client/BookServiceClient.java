package com.cts.borrowabook.client;

import com.cts.borrowabook.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BOOK-MANAGEMENT")
public interface BookServiceClient {
    
    @GetMapping("/book-api/books/{id}")
    BookDto getBookById(@PathVariable int id);
    
    @PutMapping("/book-api/books/{id}/copies")
    BookDto updateAvailableCopies(@PathVariable int id, @RequestBody int changeInCopies);
}