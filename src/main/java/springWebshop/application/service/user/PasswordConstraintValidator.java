package springWebshop.application.service.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	private Pattern pattern;
	private Matcher matcher;
	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,})";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(value);
		return matcher.matches();

	}

	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

}
