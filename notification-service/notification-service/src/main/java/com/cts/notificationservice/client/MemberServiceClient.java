package com.cts.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MEMBER-MANAGEMENT")
public interface MemberServiceClient {

    @GetMapping("/api/members/{memberId}")
    Object getMember(@PathVariable("memberId") Long memberId);
}