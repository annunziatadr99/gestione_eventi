package com.demo.gestione_eventi.exception;

public class UsernameDuplicateException extends RuntimeException {
    public UsernameDuplicateException(String message) {
        super(message);
    }
}
