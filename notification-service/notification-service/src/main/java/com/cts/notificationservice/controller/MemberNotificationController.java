package com.cts.notificationservice.controller;

import com.cts.notificationservice.dto.NotificationMapper;
import com.cts.notificationservice.dto.NotificationRequestDTO;
import com.cts.notificationservice.dto.NotificationResponseDTO;
import com.cts.notificationservice.model.Notification;
import com.cts.notificationservice.repository.NotificationRepository;
import com.cts.notificationservice.service.NotificationService;
import com.cts.notificationservice.client.TestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
public class MemberNotificationController {
    
    private final NotificationRepository repository;
    private final NotificationService notificationService;
    private final TestClient testClient;
    
    public MemberNotificationController(NotificationRepository repository, NotificationService notificationService, TestClient testClient) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.testClient = testClient;
    }
    
    @PostMapping("/notifications")
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO request) {
        try {
            if (request == null || request.getMemberId() == null || request.getMessage() == null) {
                throw new RuntimeException("Invalid request: memberId and message are required");
            }
            
            Notification notification = notificationService.createNotification(request.getMemberId(), request.getMessage());
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create notification: " + e.getMessage(), e);
        }
    }
    
    @GetMapping("/notifications/{memberId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByMemberId(@PathVariable Long memberId) {
        try {
            List<NotificationResponseDTO> notifications = repository.findByMemberIdOrderByCreatedAtDesc(memberId).stream()
                    .map(NotificationMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve notifications for member: " + e.getMessage(), e);
        }
    }
    
    @GetMapping("/test-feign")
    public ResponseEntity<String> testFeign() {
        try {
            testClient.getPost(1);
            return ResponseEntity.ok("Feign client is working!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Feign client failed: " + e.getMessage());
        }
    }
}