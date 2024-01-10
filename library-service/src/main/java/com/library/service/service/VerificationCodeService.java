package com.library.service.service;

import com.library.service.model.params.CheckEmailCodeParam;
import com.library.service.model.params.SendEmailCodeParam;
import jakarta.servlet.http.HttpServletRequest;

public interface VerificationCodeService {

    void sendCode(SendEmailCodeParam param, HttpServletRequest request);

    void checkCode(CheckEmailCodeParam param, HttpServletRequest request);

    void removeCode(CheckEmailCodeParam checkCodePayload);

}
