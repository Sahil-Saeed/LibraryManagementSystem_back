package com.cts.borrowabook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "borrowing_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "borrow_date", nullable = false)
    private Instant borrowDate;

    @Column(name = "return_date")
    private Instant returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private Double fine;

    public enum Status {
        BORROWED, RETURNED
    }
}
