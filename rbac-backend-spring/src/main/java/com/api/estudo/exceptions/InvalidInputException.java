package com.api.estudo.exceptions;

public class InvalidInputException extends BaseException {
    
    private static final long serialVersionUID = -5545978280101360675L;

    public InvalidInputException(String message) {
        super(message);
    }
}
