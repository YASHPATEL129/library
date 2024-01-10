package com.library.pojo;


import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Data
@Scope(value = "request" , proxyMode =  ScopedProxyMode.TARGET_CLASS)
@Component
public class CurrentSession {

    Long id;
    String email;
    String userName;
    String firstName;
    String lastName;
    String contact;
    Locale locale;

}
