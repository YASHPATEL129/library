package com.library.service.service;

import com.library.service.model.params.SignUpParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccountAndService {

        void signUp(SignUpParam signUpParam, HttpServletRequest request , HttpServletResponse response);
}
