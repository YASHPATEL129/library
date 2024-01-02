package com.library.service.service.impl;

import com.library.pojo.CurrentSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    @Autowired
    CurrentSession currentSession;


}
