package com.cts.notificationservice.exception;

public class DeliveryFailureException extends RuntimeException {
    public DeliveryFailureException(String message) {
        super(message);
    }
    
    public DeliveryFailureException(Throwable cause) {
        super(cause);
    }
    
    public DeliveryFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}