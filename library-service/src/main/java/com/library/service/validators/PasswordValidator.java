package com.library.service.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;
import org.passay.*;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (ObjectUtils.isEmpty(password)) {
            return false;
        }


        org.passay.PasswordValidator rule = new org.passay.PasswordValidator(
                new LengthRule(8, 100),
                new CharacterRule(EnglishCharacterData.Digit,1),
                new CharacterRule(EnglishCharacterData.Special,1),
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new CharacterRule(EnglishCharacterData.LowerCase,1)
        );

        PasswordData pass = new PasswordData(password);
       return rule.validate(pass).isValid();

    }
}