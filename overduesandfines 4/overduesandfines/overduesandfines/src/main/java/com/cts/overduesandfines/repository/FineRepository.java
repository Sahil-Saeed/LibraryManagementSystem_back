package com.cts.overduesandfines.repository;

import com.cts.overduesandfines.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByMemberId(Long memberId);
    List<Fine> findByStatus(Fine.FineStatus status);
}