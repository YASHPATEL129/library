package com.library.pojo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

@Data
public class Success<T> extends ResponseData<T> {

    private String message;

    private Object data;

    @JsonIgnore
    private String messageCode;
}
