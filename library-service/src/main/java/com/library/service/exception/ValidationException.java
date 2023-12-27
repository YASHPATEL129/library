package com.library.service.exception;

import com.library.consts.ErrorKeys;
import com.library.consts.Message;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Data
public class ValidationException extends RuntimeException{

    String error;
    String messageCode;

    public ValidationException(){
        this.messageCode = Message.SERVER_ERROR;
        this.error = ErrorKeys.SERVER_ERROR;
    }

    public ValidationException(String error, String messageCode){
        this.messageCode = messageCode;
        this.error = error;
    }

    public ValidationException(BindingResult bindingResult){
        if (bindingResult != null && bindingResult.getFieldError() != null &&
                StringUtils.contains(bindingResult.getFieldError().getDefaultMessage(), "email")){
            this.error = ErrorKeys.SERVER_ERROR;
            this.messageCode = Message.SERVER_ERROR;
        }else {
            this.error = ErrorKeys.SERVER_ERROR;
            this.messageCode = Message.SERVER_ERROR;
        }
    }


}
