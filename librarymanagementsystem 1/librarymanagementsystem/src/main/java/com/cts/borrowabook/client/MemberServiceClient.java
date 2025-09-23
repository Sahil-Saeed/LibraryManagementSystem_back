package com.cts.borrowabook.client;

import com.cts.borrowabook.dto.MemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MEMBER-MANAGEMENT")
public interface MemberServiceClient {
    
    @GetMapping("/api/members/{id}")
    MemberDto getMemberById(@PathVariable Long id);
}