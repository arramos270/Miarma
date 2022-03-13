package com.example.Miarma.validation.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Documented
public @interface UniqueUsername {
    String message() default "El nombre del producto ya existe. Por favor, intenta crear el producto con uno nuevo.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
