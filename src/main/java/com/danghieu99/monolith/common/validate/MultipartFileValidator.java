package com.danghieu99.monolith.common.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MultipartFileValidator implements ConstraintValidator<ValidMultipartFile, MultipartFile> {

    private long maxSize;
    @Valid
    private Set<@NotBlank String> allowedTypes;

    @Override
    public void initialize(ValidMultipartFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedTypes = new HashSet<>(Arrays.asList(constraintAnnotation.allowedTypes()));
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        if (file.getName().isBlank()) {
            return false;
        }
        if (file.getSize() > maxSize) {
            return false;
        }
        if (!allowedTypes.isEmpty()
                && !allowedTypes.stream().allMatch(String::isBlank)
                && !allowedTypes.contains(file.getContentType())) {
            return false;
        }
        return true;
    }
}
