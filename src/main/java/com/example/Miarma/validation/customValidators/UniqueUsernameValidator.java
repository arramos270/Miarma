package com.example.Miarma.validation.customValidators;

import com.example.Miarma.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserEntityRepository repositorio;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) { }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return StringUtils.hasText(username) && !repositorio.existsByUsername(username);
    }
}
