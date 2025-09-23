package com.cts.notificationservice.dto;

import com.cts.notificationservice.model.NotificationEvent;
import java.time.LocalDateTime;

public class NotificationResponseDTO {
    private Long id;
    private Long memberId;
    private String message;
    private NotificationEvent eventType;
    private boolean isRead;
    private String deliveryStatus;
    private Integer retryCount;
    private LocalDateTime createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public NotificationEvent getEventType() { return eventType; }
    public void setEventType(NotificationEvent eventType) { this.eventType = eventType; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}