package com.cafe.server.helper.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnumListPatternValidator implements ConstraintValidator<EnumListPattern, Set<String>> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumListPattern constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }

        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        List<String> allowedValues = Arrays.stream(enumConstants).map(Enum::toString).toList();
        return new HashSet<>(allowedValues).containsAll(values);
    }
}
