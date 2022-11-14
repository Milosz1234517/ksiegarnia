package com.example.bookstore.exceptionhandlers;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String s) {
        super(s);
    }
}
