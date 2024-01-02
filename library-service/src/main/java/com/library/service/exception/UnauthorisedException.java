package com.library.service.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Data
public class UnauthorisedException extends RuntimeException {

    public UnauthorisedException(){

    }

    public UnauthorisedException(String message) {
        super(message);
    }
}
