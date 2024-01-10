package com.library.service.service;

import com.library.pojo.response.AccountInfoResponse;
import com.library.pojo.response.AuthResponse;
import com.library.service.model.params.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccountAndService {

        void signUp(SignUpParam signUpParam, HttpServletRequest request , HttpServletResponse response);

        AuthResponse signIn(SignInParam signInParam, HttpServletRequest request , HttpServletResponse response);

        AccountInfoResponse getAccountInfo(HttpServletRequest request );

        void changePassword(ChangePasswordParam changePasswordParam , HttpServletRequest request , HttpServletResponse response);

        AccountInfoResponse changeInfo(ChangeInfoParam changeInfoParam , HttpServletRequest request , HttpServletResponse response);

        void resetPassword(ResetPasswordParam resetPasswordParam , HttpServletRequest request, HttpServletResponse response);
}
