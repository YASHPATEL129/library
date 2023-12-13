package com.library.pojo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {

    String accessToken;
    String refreshToken;
    String deviceToken;
    String email;
    String userName;
}
