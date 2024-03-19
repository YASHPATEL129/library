package com.library.service.service.impl;

import com.library.consts.AppConfigs;
import com.library.consts.ErrorKeys;
import com.library.consts.Message;
import com.library.entity.User;
import com.library.entity.WebMessage;
import com.library.enums.DeviceTypes;
import com.library.enums.IsStatus;
import com.library.interfaceProjections.UserProfileProjection;
import com.library.pojo.EmailPayload;
import com.library.pojo.response.AccountInfoResponse;
import com.library.pojo.response.AuthResponse;
import com.library.repository.UserPlanRepository;
import com.library.repository.UserRepository;
import com.library.repository.WebMessageRepository;
import com.library.service.exception.ForbiddenException;
import com.library.service.exception.InvalidCredentialsException;
import com.library.service.exception.ValidationException;
import com.library.service.hashRepository.UserSessionTrackerRepo;
import com.library.service.helper.EmailHelper;
import com.library.service.helper.SystemHelper;
import com.library.service.model.hashes.UserSessionTracker;
import com.library.service.model.params.*;
import com.library.service.service.AccountAndService;
import com.library.service.service.VerificationCodeService;
import com.library.utils.JwtUtil;
import com.library.utils.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.FailedPredicateException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class AccountAndServiceImpl extends BaseService implements AccountAndService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticateManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPlanRepository userPlanRepository;

    @Autowired
    private UserSessionTrackerRepo userSessionTrackerRepo;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailHelper emailUtil;

    @Autowired
    private WebMessageRepository webMessageRepository;

    @Autowired
    private MessageUtil messageUtil;


    @Override
    public void signUp(SignUpParam signUpParam, HttpServletRequest request, HttpServletResponse response) {

        User user = new User();
        user.setFirstName(signUpParam.getFirstName());
        user.setLastName(signUpParam.getLastName());
        user.setEmail(signUpParam.getEmail());
        user.setContact(signUpParam.getContact());
        user.setPassword(passwordEncoder.encode(signUpParam.getPassword()));
        String userName = SystemHelper.generateUsername();
        user.setUserName(userName);
        userRepository.save(user);
        sendWelcomeEmail(signUpParam, request);
    }

    @Override
    public AuthResponse signIn(SignInParam signInParam, HttpServletRequest request, HttpServletResponse response) {

        try{
            authenticateManager.authenticate(new UsernamePasswordAuthenticationToken(signInParam.getEmail(), signInParam.getPassword()));
        } catch (AuthenticationException ex){
            throw new InvalidCredentialsException(Message.INCORRECT_ACCOUNT_OR_PASSWORD, ErrorKeys.INCORRECT_ACCOUNT_OR_PASSWORD);
        }

        AuthResponse authResponse = jwtUtil.generateToken(signInParam.getEmail(), request, response);
        User user = userRepository.findByEmail(signInParam.getEmail());
        authResponse.setUserName(user.getUserName());
        authResponse.setEmail(user.getEmail());
        authResponse.setFirstName(user.getFirstName());
        authResponse.setLastName(user.getLastName());
        authResponse.setContact(user.getContact());
        configureSession(signInParam.getEmail(), authResponse.getDeviceToken(), request.getHeader(AppConfigs.DEVICE_TYPE_NAME));
        return authResponse;
    }

    @Override
    public AccountInfoResponse getAccountInfo(HttpServletRequest request) {
        User user = userRepository.findByEmail(currentSession.getEmail());
        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
        accountInfoResponse.setUserName(user.getUserName());
        accountInfoResponse.setFirstName(user.getFirstName());
        accountInfoResponse.setLastName(user.getLastName());
        accountInfoResponse.setEmail(user.getEmail());
        accountInfoResponse.setContact(user.getContact());
        UserProfileProjection plan = userPlanRepository.getUserProfileDetails(user.getUserName(), IsStatus.CURRENT.toString()).stream().findFirst().orElse(null);
        if(ObjectUtils.isNotEmpty(plan))
            accountInfoResponse.setPlanName(plan.getPlanName());
        return accountInfoResponse;

    }

    @Override
    public void changePassword(ChangePasswordParam changePasswordParam, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(currentSession.getEmail());
        if (StringUtils.equals(changePasswordParam.getCurrentPassword(), changePasswordParam.getNewPassword())) {
            throw new ForbiddenException(Message.SAME_PASSWORD, ErrorKeys.SAME_PASSWORD);
        }
        validateCurrentPassword(changePasswordParam.getCurrentPassword(), user.getPassword());
        try {
            user.setPassword(passwordEncoder.encode(changePasswordParam.getNewPassword()));
            userRepository.save(user);
        }catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @Override
    public AccountInfoResponse changeInfo(ChangeInfoParam changeInfoParam, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail((currentSession.getEmail()));
        if (user == null) {
            throw new ForbiddenException(Message.INCORRECT_ACCOUNT_OR_PASSWORD , ErrorKeys.INCORRECT_ACCOUNT_OR_PASSWORD);
        }
        try {
            user.setFirstName(changeInfoParam.getFirstName());
            user.setLastName(changeInfoParam.getLastName());
            user.setContact(changeInfoParam.getContact());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        User user1 = userRepository.findByEmail(currentSession.getEmail());
        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
        accountInfoResponse.setUserName(user1.getUserName());
        accountInfoResponse.setFirstName(user1.getFirstName());
        accountInfoResponse.setLastName(user1.getLastName());
        accountInfoResponse.setEmail(user1.getEmail());
        accountInfoResponse.setContact(user1.getContact());
        return accountInfoResponse;

    }

    @Override
    public void resetPassword(ResetPasswordParam resetPasswordParam, HttpServletRequest request, HttpServletResponse response) {
        CheckEmailCodeParam checkCodePayLoad = new CheckEmailCodeParam().setEmail(resetPasswordParam.getEmail()).setCode(resetPasswordParam.getCode());
        verificationCodeService.checkCode(checkCodePayLoad, request);
        User user = userRepository.findByEmail(resetPasswordParam.getEmail());
        if (user != null) {
            if (passwordEncoder.matches(resetPasswordParam.getPassword(), user.getPassword())) {
                throw new ForbiddenException(Message.SAME_PASSWORD, ErrorKeys.SAME_PASSWORD);
            }

            user.setPassword(passwordEncoder.encode(resetPasswordParam.getPassword()));
            userRepository.save(user);
            verificationCodeService.removeCode(checkCodePayLoad);
            return;
        }
        throw new ForbiddenException(Message.PASSWORD_RESET_FAILED, ErrorKeys.PASSWORD_RESET_FAILED);
    }

    @Override
    public void getWebMessage(WebMessage webMessageReq, HttpServletRequest request, HttpServletResponse response) {
        WebMessage webMessage = new WebMessage();
        webMessage.setName(webMessageReq.getName());
        webMessage.setEmail(webMessageReq.getEmail());
        webMessage.setMessage(webMessageReq.getMessage());
        webMessageRepository.save(webMessage);

    }

    private void configureSession(String email , String deviceVerificationToken , String deviceType){
        UserSessionTracker session = userSessionTrackerRepo.getSession(email , deviceType);
            session = new UserSessionTracker();
            session.setUserEmail(email);
            session.setVerificationId(deviceVerificationToken);
            try {
                session.setDeviceType(DeviceTypes.valueOf(deviceType));
            } catch (Exception e) {
                throw new ValidationException();
            }
        userSessionTrackerRepo.save(session);
    }

    private void validateCurrentPassword(String rawPassword , String password) {
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw new ForbiddenException(Message.INCORRECT_CURRENT_PASSWORD, ErrorKeys.INCORRECT_CURRENT_PASSWORD);
        }
    }

    public void sendWelcomeEmail(SignUpParam param, HttpServletRequest request) {
        Boolean accountExists = userRepository.existsByEmail(param.getEmail());

        Map<String, Object> emailProps = new HashMap<String, Object>(){{
            put("subject", Message.VERIFICATION_CODE_EMAIL_SUBJECT_RESET_PASSWORD);
            put("email", param.getEmail());
        }};
        Boolean isSend = emailUtil.send(new EmailPayload()
                .setSendTo(param.getEmail())
                .setTemplateCode(AppConfigs.WELCOME_TEMPLATE_HTML_KEY)
                .setSubject(messageUtil.getMessage(Message.WELCOME_EMAIL_SUBJECT,request.getLocale()))
                .setProperties(emailProps));
        if (!isSend){
            throw new ForbiddenException(Message.TRY_AGAIN_LATER, ErrorKeys.TRY_AGAIN_LATER);
        }
    }
}
