package com.cts.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "test-client", url = "https://jsonplaceholder.typicode.com")
public interface TestClient {
    
    @GetMapping("/posts/{id}")
    String getPost(@PathVariable("id") int id);
}