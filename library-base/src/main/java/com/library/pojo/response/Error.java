package com.library.pojo.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error<T> extends ResponseData<T> {

    private String message;

    private String error;

    private Object data;

    @JsonIgnore
    private String messageCode;

    @JsonIgnore
    private Object[] args;
}
