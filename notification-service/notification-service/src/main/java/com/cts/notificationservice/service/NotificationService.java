package com.cts.notificationservice.service;

import com.cts.notificationservice.model.Notification;
import com.cts.notificationservice.model.NotificationEvent;
import com.cts.notificationservice.model.DeliveryStatus;
import com.cts.notificationservice.repository.NotificationRepository;
import com.cts.notificationservice.exception.NotificationNotFoundException;
import com.cts.notificationservice.exception.DeliveryFailureException;
import com.cts.notificationservice.exception.ValidationException;
import com.cts.notificationservice.client.MemberServiceClient;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.List;

@Service
public class NotificationService {
    
    private final NotificationRepository repository;
    private final SecureRandom secureRandom;
    
    private final MemberServiceClient memberServiceClient;
    
    public NotificationService(NotificationRepository repository, 
                             MemberServiceClient memberServiceClient) {
        this.repository = repository;
        this.secureRandom = new SecureRandom();
        this.memberServiceClient = memberServiceClient;
    }
    
    public Notification createNotification(Long memberId, String message) {
        if (memberId == null) {
            throw new ValidationException("Member ID cannot be null");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new ValidationException("Message cannot be null or empty");
        }
        
        // Validate member exists (gracefully handles service unavailability)
        try {
            memberServiceClient.getMember(memberId);
            System.out.println("Member found");
        } catch (Exception e) {
            System.out.println("Member service unavailable, assuming member exists: " + e.getMessage());
        }
        
        Notification notification = new Notification();
        notification.setMemberId(memberId);
        notification.setMessage(message);
        notification.setDeliveryStatus(DeliveryStatus.PENDING);
        notification.setRetryCount(0);
        return repository.save(notification);
    }
    
    public Notification detectAndTriggerDueDateNotifications(Long memberId, String bookTitle) {
        String message = "Book '" + bookTitle + "' is due tomorrow";
        return triggerEventNotification(memberId, NotificationEvent.DUE_DATE, message);
    }
    
    public Notification detectAndTriggerOverdueNotifications(Long memberId, String bookTitle) {
        String message = "Book '" + bookTitle + "' is overdue. Please return immediately to avoid penalties";
        return triggerEventNotification(memberId, NotificationEvent.OVERDUE, message);
    }
    
    public Notification detectAndTriggerReservationNotifications(Long memberId, String bookTitle) {
        String message = "Your reserved book '" + bookTitle + "' is now available for pickup";
        return triggerEventNotification(memberId, NotificationEvent.RESERVATION_AVAILABLE, message);
    }
    
    public Notification sendFineNotification(Long memberId, String bookTitle, Double fineAmount) {
        String message = "Fine of $" + fineAmount + " has been applied for overdue book '" + bookTitle + "'";
        return triggerEventNotification(memberId, NotificationEvent.FINE_NOTICE, message);
    }
    
    public Notification sendPaymentReminder(Long memberId, Double totalFines) {
        String message = "Payment reminder: You have outstanding fines totaling $" + totalFines + ". Please pay to avoid account suspension.";
        return triggerEventNotification(memberId, NotificationEvent.PAYMENT_REMINDER, message);
    }
    
    public Notification retryDelivery(Long notificationId) {
        Notification notification = repository.findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));
        
        int currentRetryCount = notification.getRetryCount() != null ? notification.getRetryCount() : 0;
        
        if (currentRetryCount >= 3) {
            throw new DeliveryFailureException("Maximum retry attempts (3) reached for notification: " + notificationId);
        }
        
        try {
            simulateDelivery();
            notification.setDeliveryStatus(DeliveryStatus.SENT);
        } catch (DeliveryFailureException e) {
            notification.setRetryCount(currentRetryCount + 1);
            if (notification.getRetryCount() >= 3) {
                notification.setDeliveryStatus(DeliveryStatus.FAILED);
            } else {
                switch (notification.getRetryCount()) {
                    case 1 -> notification.setDeliveryStatus(DeliveryStatus.RETRY_1);
                    case 2 -> notification.setDeliveryStatus(DeliveryStatus.RETRY_2);
                    case 3 -> notification.setDeliveryStatus(DeliveryStatus.RETRY_3);
                    default -> notification.setDeliveryStatus(DeliveryStatus.FAILED);
                }
            }
        }
        
        return repository.save(notification);
    }
    
    private Notification triggerEventNotification(Long memberId, NotificationEvent eventType, String customMessage) {
        Notification notification = new Notification();
        notification.setMemberId(memberId);
        notification.setEventType(eventType);
        notification.setMessage(customMessage);
        notification.setDeliveryStatus(DeliveryStatus.PENDING);
        notification.setRetryCount(0);
        
        try {
            simulateDelivery();
            notification.setDeliveryStatus(DeliveryStatus.SENT);
        } catch (DeliveryFailureException e) {
            notification.setDeliveryStatus(DeliveryStatus.FAILED);
        }
        
        return repository.save(notification);
    }
    
    private void simulateDelivery() {
        if (secureRandom.nextDouble() < 0.3) {
            throw new DeliveryFailureException("Delivery failed: Service unavailable");
        }
    }
    
    public void processFailedNotifications() {
        List<Notification> failedNotifications = repository.findByDeliveryStatus(DeliveryStatus.FAILED);
        for (Notification notification : failedNotifications) {
            if (notification.getRetryCount() < 3) {
                try {
                    retryDelivery(notification.getId());
                } catch (Exception e) {
                    // Log error but continue processing other notifications
                }
            }
        }
    }
}