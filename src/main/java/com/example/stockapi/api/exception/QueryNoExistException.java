package com.example.stockapi.api.exception;

import org.hibernate.QueryException;

public class QueryNoExistException extends QueryException {
    public QueryNoExistException(String message) {
        super(message);
    }
}
