package com.cts.notificationservice.dto;

import com.cts.notificationservice.model.Notification;

public class NotificationMapper {
    
    public static NotificationResponseDTO toResponseDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setMemberId(notification.getMemberId());
        dto.setMessage(notification.getMessage());
        dto.setEventType(notification.getEventType());
        dto.setRead(notification.isRead());
        dto.setDeliveryStatus(notification.getDeliveryStatus().name());
        dto.setRetryCount(notification.getRetryCount());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
    
    public static Notification toEntity(NotificationRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("NotificationRequestDTO cannot be null");
        }
        
        Notification notification = new Notification();
        notification.setMemberId(dto.getMemberId());
        notification.setMessage(dto.getMessage());
        return notification;
    }
}