package com.cts.lms.exception;

public class MemberServiceException extends RuntimeException {
    public MemberServiceException(String message) {
        super(message);
    }
    
    public MemberServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}