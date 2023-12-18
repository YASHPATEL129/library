package com.library.service.model.params;

import com.library.service.validators.ValidEmail;
import com.library.service.validators.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpParam {


    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    private String contact;

    @ValidPassword
    private String password;

    @NotBlank
    private String confirmPassword;
}
