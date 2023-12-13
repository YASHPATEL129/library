package com.library.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.library.consts.AppConfigs;
import com.library.enums.TokenType;
import com.library.pojo.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class JwtUtil {


    @Value("${myapp.secret}")
    String secret;

    @Value("${jwt.access.expiry.days}")
    long authExpiry;

    @Value("${jwt.refresh.expiry.days}")
    long refreshExpiry;

    @Autowired
    Crypto crypto;


    public AuthResponse generateToken(String userName, HttpServletRequest request, HttpServletResponse response){
        AuthResponse tokens = new AuthResponse();
        String jwtId = UUID.randomUUID().toString();
        tokens.setAccessToken(generateToken(userName, request.getRequestURI().toString(), jwtId , TokenType.AUTH_TOKEN ));
        tokens.setRefreshToken(generateToken(userName, request.getRequestURI().toString(), jwtId , TokenType.REFRESH_TOKEN));
        tokens.setDeviceToken(String.valueOf(UUID.randomUUID()));
        return tokens;
    }

    private String generateToken(String userName, String issuer , String validationToken , TokenType type){
        String token = JWT.create().withSubject(userName)
                .withClaim(AppConfigs.JWT_ID, validationToken)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis((TokenType.AUTH_TOKEN.equals(type) ? authExpiry : refreshExpiry))))
                .withIssuer(issuer)
                .sign(getJwtAlgo());
        return crypto.encrypt(token);
    }


    public Algorithm getJwtAlgo() {
        return Algorithm.HMAC256(secret.getBytes());
    }

    public String extractUserName(String decryptedToken){
        return JWT.decode(decryptedToken).getSubject();
    }

    public String extractJWTid(String decryptedToken){
        return JWT.decode(decryptedToken).getClaim(AppConfigs.JWT_ID).toString();
    }


}

