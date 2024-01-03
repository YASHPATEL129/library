package com.library.service.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    String error;
    Object[] args;

    public ForbiddenException() {}

    public ForbiddenException(String message){
        super(message);
    }

    public ForbiddenException(String message, String error){
        super(message);
        this.error = error;
    }

    public ForbiddenException(String message, String error, Object[] args){
        super(message);
        this.error = error;
        this.args = args;
    }
}
