package com.github.aivachkin.socialmedia.exception;

public class SubscriptionDoesNotExistException extends RuntimeException {
    public SubscriptionDoesNotExistException(String message) {
        super(message);
    }
}
