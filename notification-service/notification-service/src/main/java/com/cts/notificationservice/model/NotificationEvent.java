package com.cts.notificationservice.model;

public enum NotificationEvent {
    DUE_DATE("Book due date approaching"),
    OVERDUE("Book is overdue"),
    RESERVATION_AVAILABLE("Reserved book is now available"),
    FINE_NOTICE("Fine notice for overdue items"),
    PAYMENT_REMINDER("Payment reminder for outstanding fines");
    
    private final String description;
    
    NotificationEvent(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}