package com.cts.borrowabook.repository;

import com.cts.borrowabook.entity.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {
    long countByMemberIdAndStatus(Long memberId, BorrowTransaction.Status status);
    
    List<BorrowTransaction> findByMemberId(Long memberId);
    
    @Query("SELECT bt FROM BorrowTransaction bt WHERE bt.status = 'BORROWED' AND bt.borrowDate < :cutoffDate")
    List<BorrowTransaction> findOverdueBorrowings(@Param("cutoffDate") Instant cutoffDate);
}
