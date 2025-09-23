package com.cts.notificationservice.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
    
    public NotificationNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}