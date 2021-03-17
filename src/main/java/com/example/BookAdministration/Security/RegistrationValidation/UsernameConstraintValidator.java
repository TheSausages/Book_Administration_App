package com.example.BookAdministration.Security.RegistrationValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UsernameConstraintValidator implements ConstraintValidator<ValidUsername, String> {
    Pattern USERNAMEPATTERN = Pattern.compile("^[A-Z](?=.*[a-zA-Z])(?=.*[0-9]).{5,29}?");
    
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return USERNAMEPATTERN.matcher(value).matches();
    }
}
