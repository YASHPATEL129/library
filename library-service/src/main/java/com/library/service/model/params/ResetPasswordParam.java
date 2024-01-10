package com.library.service.model.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordParam {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String code;
}
