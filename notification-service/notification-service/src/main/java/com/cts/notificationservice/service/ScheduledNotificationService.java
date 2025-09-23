package com.cts.notificationservice.service;

import com.cts.notificationservice.model.Notification;
import com.cts.notificationservice.model.NotificationEvent;
import com.cts.notificationservice.model.DeliveryStatus;
import com.cts.notificationservice.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledNotificationService {
    
    private final NotificationRepository repository;
    private final NotificationService notificationService;
    
    public ScheduledNotificationService(NotificationRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }
    
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void checkDueDates() {
        // Simulate checking for due dates and sending notifications
        System.out.println("Checking for due dates at: " + LocalDateTime.now());
        
        // Example: Create due date notifications for members
        try {
            notificationService.detectAndTriggerDueDateNotifications(1L, "Sample Book - Due Tomorrow");
        } catch (Exception e) {
            System.err.println("Error in due date check: " + e.getMessage());
        }
    }
    
    @Scheduled(fixedRate = 600000) // Every 10 minutes
    public void checkOverdueItems() {
        System.out.println("Checking for overdue items at: " + LocalDateTime.now());
        
        try {
            notificationService.detectAndTriggerOverdueNotifications(1L, "Sample Book - Overdue");
        } catch (Exception e) {
            System.err.println("Error in overdue check: " + e.getMessage());
        }
    }
    
    @Scheduled(fixedRate = 900000) // Every 15 minutes
    public void processFailedNotifications() {
        System.out.println("Processing failed notifications at: " + LocalDateTime.now());
        
        try {
            notificationService.processFailedNotifications();
        } catch (Exception e) {
            System.err.println("Error processing failed notifications: " + e.getMessage());
        }
    }
    
    @Scheduled(cron = "0 0 9 * * ?") // Daily at 9 AM
    public void sendDailyReminders() {
        System.out.println("Sending daily reminders at: " + LocalDateTime.now());
        
        try {
            // Send daily summary notifications
            notificationService.createNotification(1L, "Daily Reminder: Check your library account for any pending items.");
        } catch (Exception e) {
            System.err.println("Error sending daily reminders: " + e.getMessage());
        }
    }
}