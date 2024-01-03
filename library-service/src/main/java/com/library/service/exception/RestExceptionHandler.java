package com.library.service.exception;

import com.library.consts.ErrorKeys;
import com.library.consts.Message;
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

    @ExceptionHandler(InvalidCredentialsException.class)
    public final ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request){
        Error<?> responseData = new Error<>();
        responseData.setMessageCode(ex.getMessage());
        responseData.setError(ex.getError());
        return buildResponse(responseData , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorisedException.class)
    public final ResponseEntity<Object> handleUnauthorisedException(InvalidCredentialsException ex, WebRequest request){
        Error<?> responseData = new Error<>();
        responseData.setMessageCode(Message.UNAUTHORIZED);
        responseData.setError(ErrorKeys.UNAUTHORIZED);
        return buildResponse(responseData , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<Object> handForbiddenException(ForbiddenException ex, WebRequest request){
        Error<?> responseData = new Error<>();
        responseData.setMessageCode(ex.getMessage());
        responseData.setError((ex.getError() != null) ? ex.getError() : HttpStatus.FORBIDDEN.name());
        responseData.setArgs(ex.getArgs());
        return buildResponse(responseData , HttpStatus.FORBIDDEN);
    }
}
