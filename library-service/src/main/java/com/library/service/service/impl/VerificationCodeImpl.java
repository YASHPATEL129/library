package com.library.service.service.impl;

import com.library.consts.AppConfigs;
import com.library.consts.ErrorKeys;
import com.library.consts.Message;
import com.library.pojo.CurrentSession;
import com.library.pojo.EmailPayload;
import com.library.repository.UserRepository;
import com.library.service.exception.ForbiddenException;
import com.library.service.exception.ValidationException;
import com.library.service.hashRepository.EmailCodeRepo;
import com.library.service.hashRepository.EmailCodeRequestTrackerRepo;
import com.library.service.hashRepository.EmailRequestTrackerRepo;
import com.library.service.helper.EmailHelper;
import com.library.service.helper.SystemHelper;
import com.library.service.model.hashes.EmailCode;
import com.library.service.model.hashes.EmailCodeRequestTracker;
import com.library.service.model.hashes.EmailRequestTracker;
import com.library.service.model.params.CheckEmailCodeParam;
import com.library.service.model.params.SendEmailCodeParam;
import com.library.service.service.VerificationCodeService;
import com.library.utils.IpAddressUtil;
import com.library.utils.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.library.consts.AppConfigs.*;

@Service
public class VerificationCodeImpl implements VerificationCodeService {

    @Autowired
    EmailHelper emailUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailCodeRepo codeRepo;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    EmailRequestTrackerRepo emailRequestTrackerRepo;

    @Autowired
    EmailCodeRequestTrackerRepo reqTrackerRepo;

    @Override
    public void sendCode(SendEmailCodeParam param, HttpServletRequest request) {
        Boolean accountExists = userRepository.existsByEmail(param.getEmail());
        validateRequest(param, request);

        Map<String, Object> emailProps = new HashMap<String, Object>(){{
           put("subject", Message.VERIFICATION_CODE_EMAIL_SUBJECT_RESET_PASSWORD);
           put("verificationCode", generateVerificationCode(param, request));
        }};
        Boolean isSend = emailUtil.send(new EmailPayload()
                .setSendTo(param.getEmail())
                .setTemplateCode(AppConfigs.EMAIL_CODE_TEMPLATE_HTML_KEY)
                .setSubject(Message.VERIFICATION_CODE_EMAIL_SUBJECT_RESET_PASSWORD)
                .setProperties(emailProps));

        if (!isSend){
            throw new ForbiddenException(Message.TRY_AGAIN_LATER, ErrorKeys.TRY_AGAIN_LATER);
        }
        updateVerificationReqTrack(param, request);
    }

    @Override
    public void checkCode(CheckEmailCodeParam param, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(param.getCode()) || StringUtils.isEmpty(param.getCode())){
            throw new ValidationException();
        }
        EmailCode codeRes = codeRepo.getEmailCode(param.getEmail());
        if (codeRes == null || !StringUtils.equals(codeRes.getCode(), param.getCode())) {
            throw new ForbiddenException(Message.INVALID_VERIFICATION_CODE, ErrorKeys.INVALID_VERIFICATION_CODE);
        }
    }

    @Override
    public void removeCode(CheckEmailCodeParam checkCodePayload) {
        EmailCode codeRes = codeRepo.getEmailCode(checkCodePayload.getEmail());

        if (codeRes != null) {
            codeRepo.delete(codeRes);
        }
    }

    public String generateVerificationCode(SendEmailCodeParam param, HttpServletRequest request) {
        EmailCode code = codeRepo.getEmailCode(param.getEmail());
        if (code == null){
            code = new EmailCode()
                    .setEmail(param.getEmail())
                    .setCode(SystemHelper.generateVerificationCode());

        } else {
            code.setCode(SystemHelper.generateVerificationCode());
        }
        codeRepo.save(code);
        return code.getCode();
    }

    private void validateRequest(SendEmailCodeParam param, HttpServletRequest request){
        validateHost(request);
        Instant now = Instant.now();
        EmailCodeRequestTracker emailReq = reqTrackerRepo.getTrack(IpAddressUtil.getIp(request), param.getEmail());
        if (emailReq != null && Duration.between(emailReq.getTimestamp().toInstant(), now).getSeconds() < EMAIL_CODE_INTERVAL_SECONDS) {
            throw new ForbiddenException(Message.TRY_AGAIN_LATER, ErrorKeys.TRY_AGAIN_LATER);
        }
        if (emailReq != null && emailReq.getAttempts() >= EMAIL_CODE_DAILY_NUMBER_ACCOUNT){
            throw new ForbiddenException(Message.SEND_FAILED_TRY_AGAIN_TOMORROW, ErrorKeys.TRY_AGAIN_TOMORROW);
        }
    }

    private void updateVerificationReqTrack(SendEmailCodeParam param, HttpServletRequest request){
        Instant now = Instant.now();
        EmailCodeRequestTracker emailReq = reqTrackerRepo.getTrack(IpAddressUtil.getIp(request), param.getEmail());
        if (emailReq == null){
            emailReq = new EmailCodeRequestTracker()
                    .setEmail(param.getEmail())
                    .setIp(IpAddressUtil.getIp(request));
        } else{
            emailReq.setAttempts(emailReq.getAttempts() + 1);
        }
        emailReq.setTimestamp(Date.from(now));
        reqTrackerRepo.save(emailReq);
        updateHost(request);
    }

    private void validateHost(HttpServletRequest request){
        String ip = IpAddressUtil.getIp(request);
        EmailRequestTracker ipReq = emailRequestTrackerRepo.getTrack(ip);
        if (ipReq != null && ipReq.getCount() >= EMAIL_CODE_DAILY_NUMBER_IP) {
            throw new ForbiddenException(Message.SEND_FAILED_TRY_AGAIN_TOMORROW, ErrorKeys.TRY_AGAIN_TOMORROW);
        }
    }

    private void updateHost(HttpServletRequest request){
        String ip = IpAddressUtil.getIp(request);
        EmailRequestTracker inReq = emailRequestTrackerRepo.getTrack(ip);
        if (inReq == null){
            inReq = new EmailRequestTracker()
                    .setCount(1)
                    .setIp(ip);
        } else {
            inReq.setCount(inReq.getCount() + 1);
        }
        emailRequestTrackerRepo.save(inReq);
    }
}
