package com.library.service.service.impl;

import com.library.entity.User;
import com.library.repository.UserRepository;
import com.library.service.helper.SystemHelper;
import com.library.service.model.params.SignUpParam;
import com.library.service.service.AccountAndService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AccountAndServiceImpl implements AccountAndService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
