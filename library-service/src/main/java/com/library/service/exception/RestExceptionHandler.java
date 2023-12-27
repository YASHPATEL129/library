package com.library.service.exception;

import com.library.pojo.response.Error;
import com.library.pojo.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponse (ResponseData<?> responseData, HttpStatus httpStatus){
        return new ResponseEntity<>(responseData, httpStatus);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        Error<?> responseData = new Error<>();
        responseData.setMessageCode(ex.getMessageCode());
        responseData.setError(ex.getError());
        return buildResponse(responseData , HttpStatus.BAD_REQUEST);
    }
}
