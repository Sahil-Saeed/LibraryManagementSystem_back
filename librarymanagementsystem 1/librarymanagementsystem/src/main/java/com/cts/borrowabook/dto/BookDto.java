package com.cts.borrowabook.dto;

import lombok.Data;

@Data
public class BookDto {
    private int id;
    private String title;
    private String author;
    private int availableCopies;
}