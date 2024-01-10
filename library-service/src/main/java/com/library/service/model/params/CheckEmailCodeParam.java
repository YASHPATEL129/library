package com.library.service.model.params;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckEmailCodeParam {

    @NotBlank
    private String email;

    @NotBlank
    private String code;

}