package com.library.service.service.impl;

import com.library.consts.AppConfigs;
import com.library.consts.ErrorKeys;
import com.library.consts.Message;
import com.library.entity.User;
import com.library.enums.DeviceTypes;
import com.library.pojo.response.AccountInfoResponse;
import com.library.pojo.response.AuthResponse;
import com.library.repository.UserRepository;
import com.library.service.exception.ForbiddenException;
import com.library.service.exception.InvalidCredentialsException;
import com.library.service.exception.ValidationException;
import com.library.service.hashRepository.UserSessionTrackerRepo;
import com.library.service.helper.SystemHelper;
import com.library.service.model.hashes.UserSessionTracker;
import com.library.service.model.params.ChangePasswordParam;
import com.library.service.model.params.SignInParam;
import com.library.service.model.params.SignUpParam;
import com.library.service.service.AccountAndService;
import com.library.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;


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
    private UserSessionTrackerRepo userSessionTrackerRepo;


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
}
