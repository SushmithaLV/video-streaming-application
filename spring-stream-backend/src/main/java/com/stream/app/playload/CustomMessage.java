package com.stream.app.playload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


public class CustomMessage {
	private String message;
	private boolean success = false;
	
	// No-arg constructor
    public CustomMessage() {
    }
    
 // All-args constructor
    public CustomMessage(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	
	
	
}
