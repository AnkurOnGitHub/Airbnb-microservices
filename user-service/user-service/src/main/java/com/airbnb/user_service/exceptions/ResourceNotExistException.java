package com.airbnb.user_service.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotExistException extends ApiException{
    public ResourceNotExistException(String message) {
        super(message,HttpStatus.NOT_FOUND);
    }
}
