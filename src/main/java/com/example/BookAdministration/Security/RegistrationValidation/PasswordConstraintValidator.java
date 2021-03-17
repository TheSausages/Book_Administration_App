package com.example.BookAdministration.Security.RegistrationValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    Pattern PASSWORDPATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{6,18}$");
    Pattern ENCODEDPASSWORDPATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        //User cannot enter password longer that 18 chars (checked in service), needed to save encoded password

        return PASSWORDPATTERN.matcher(value).matches() || ENCODEDPASSWORDPATTERN.matcher(value).matches();
    }
}
