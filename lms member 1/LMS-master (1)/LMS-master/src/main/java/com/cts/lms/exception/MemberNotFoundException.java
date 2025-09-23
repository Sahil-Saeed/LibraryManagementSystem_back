package com.cts.lms.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
    
    public MemberNotFoundException(Long id) {
        super("Member not found with ID: " + id);
    }
}