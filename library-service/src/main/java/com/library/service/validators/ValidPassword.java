package com.library.service.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Documented
@Target({ElementType.TYPE , ElementType.FIELD ,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "password is weak";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
