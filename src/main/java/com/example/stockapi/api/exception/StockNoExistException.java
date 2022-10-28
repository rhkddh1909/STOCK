package com.example.stockapi.api.exception;

public class StockNoExistException extends RuntimeException{
    public StockNoExistException(String message) {
        super(message);
    }
}
