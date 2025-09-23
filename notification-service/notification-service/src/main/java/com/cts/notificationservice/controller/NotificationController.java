package com.cts.notificationservice.controller;

import com.cts.notificationservice.dto.NotificationMapper;
import com.cts.notificationservice.dto.NotificationRequestDTO;
import com.cts.notificationservice.dto.NotificationResponseDTO;
import com.cts.notificationservice.model.Notification;
import com.cts.notificationservice.model.DeliveryStatus;
import com.cts.notificationservice.repository.NotificationRepository;
import com.cts.notificationservice.service.NotificationService;
import com.cts.notificationservice.exception.NotificationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    private final NotificationRepository repository;
    private final NotificationService notificationService;
    
    public NotificationController(NotificationRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }
    
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO request) {
        Notification notification = notificationService.createNotification(request.getMemberId(), request.getMessage());
        return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
    }
    
    @PostMapping("/trigger/due-date")
    public ResponseEntity<NotificationResponseDTO> triggerDueDateNotification(@RequestBody NotificationRequestDTO request) {
        try {
            Notification notification = notificationService.detectAndTriggerDueDateNotifications(request.getMemberId(), request.getBookTitle());
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to trigger due date notification", e);
        }
    }
    
    @PostMapping("/trigger/overdue")
    public ResponseEntity<NotificationResponseDTO> triggerOverdueNotification(@RequestBody NotificationRequestDTO request) {
        try {
            Notification notification = notificationService.detectAndTriggerOverdueNotifications(request.getMemberId(), request.getBookTitle());
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to trigger overdue notification", e);
        }
    }
    
    @PostMapping("/trigger/reservation")
    public ResponseEntity<NotificationResponseDTO> triggerReservationNotification(@RequestBody NotificationRequestDTO request) {
        try {
            Notification notification = notificationService.detectAndTriggerReservationNotifications(request.getMemberId(), request.getBookTitle());
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to trigger reservation notification", e);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        try {
            List<NotificationResponseDTO> notifications = repository.findAll().stream()
                    .map(NotificationMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve notifications", e);
        }
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<NotificationResponseDTO>> getMemberNotifications(@PathVariable Long memberId) {
        try {
            List<NotificationResponseDTO> notifications = repository.findByMemberIdOrderByCreatedAtDesc(memberId).stream()
                    .map(NotificationMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve member notifications", e);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new NotificationNotFoundException("Cannot delete notification with id: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.ok("Notification deleted successfully");
    }
    
    @PostMapping("/retry/{id}")
    public ResponseEntity<NotificationResponseDTO> retryNotificationDelivery(@PathVariable Long id) {
        Notification notification = notificationService.retryDelivery(id);
        return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
    }
    
    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String, Object>> getNotificationStatus(@PathVariable Long id) {
        Notification notification = repository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        
        Map<String, Object> status = new HashMap<>();
        status.put("id", notification.getId());
        status.put("deliveryStatus", notification.getDeliveryStatus());
        Integer retryCount = notification.getRetryCount() != null ? notification.getRetryCount() : 0;
        status.put("retryCount", retryCount);
        status.put("canRetry", retryCount < 3 && DeliveryStatus.FAILED.equals(notification.getDeliveryStatus()));
        
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is running!");
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable Long id) {
        try {
            Notification notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
            notification.setRead(true);
            Notification updated = repository.save(notification);
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(updated));
        } catch (Exception e) {
            throw new RuntimeException("Failed to mark notification as read", e);
        }
    }
    
    @GetMapping("/member/{memberId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications(@PathVariable Long memberId) {
        try {
            List<NotificationResponseDTO> notifications = repository.findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(memberId).stream()
                    .map(NotificationMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve unread notifications", e);
        }
    }
    
    @GetMapping("/member/{memberId}/stats")
    public ResponseEntity<Map<String, Long>> getMemberNotificationStats(@PathVariable Long memberId) {
        try {
            List<Notification> allNotifications = repository.findByMemberIdOrderByCreatedAtDesc(memberId);
            
            Map<String, Long> stats = new HashMap<>();
            stats.put("total", (long) allNotifications.size());
            stats.put("unread", allNotifications.stream().filter(n -> !n.isRead()).count());
            stats.put("failed", allNotifications.stream().filter(n -> DeliveryStatus.FAILED.equals(n.getDeliveryStatus())).count());
            stats.put("sent", allNotifications.stream().filter(n -> DeliveryStatus.SENT.equals(n.getDeliveryStatus())).count());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve notification statistics", e);
        }
    }
    
    @PostMapping("/send-fine")
    public ResponseEntity<NotificationResponseDTO> sendFineNotification(@RequestBody Map<String, Object> request) {
        try {
            Long memberId = Long.valueOf(request.get("memberId").toString());
            String bookTitle = request.get("bookTitle").toString();
            Double fineAmount = Double.valueOf(request.get("fineAmount").toString());
            
            Notification notification = notificationService.sendFineNotification(memberId, bookTitle, fineAmount);
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send fine notification", e);
        }
    }
    
    @GetMapping("/test-resttemplate")
    public ResponseEntity<Map<String, Object>> testRestTemplate() {
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            String response = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", String.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "RestTemplate is working");
            result.put("externalApiResponse", response);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "RestTemplate failed: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    @PostMapping("/send-payment-reminder")
    public ResponseEntity<NotificationResponseDTO> sendPaymentReminder(@RequestBody Map<String, Object> request) {
        try {
            Long memberId = Long.valueOf(request.get("memberId").toString());
            Double totalFines = Double.valueOf(request.get("totalFines").toString());
            
            Notification notification = notificationService.sendPaymentReminder(memberId, totalFines);
            return ResponseEntity.ok(NotificationMapper.toResponseDTO(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send payment reminder", e);
        }
    }
    

    

}