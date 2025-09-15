package com.epam.learn.trsessionservice.infra.validation.validator;

import com.epam.learn.trsessionservice.infra.validation.EnumNamePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {

  private Pattern pattern;

  @Override
  public void initialize(EnumNamePattern constraintAnnotation) {
    try {
      pattern = Pattern.compile(constraintAnnotation.regexp());
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid regex pattern", e);
    }
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    if (value == null) return false;
    Matcher matcher = pattern.matcher(value.name());
    return matcher.matches();
  }
}
