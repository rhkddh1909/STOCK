package com.example.stockapi.api.exception;

public class QueryNoExistException extends RuntimeException{
    public QueryNoExistException(String message) {
        super(message);
    }
}
