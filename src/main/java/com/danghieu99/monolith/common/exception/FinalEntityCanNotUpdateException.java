package com.danghieu99.monolith.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class FinalEntityCanNotUpdateException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String resourceName;
    private final String fieldName;
    private final Object filedValue;

    public FinalEntityCanNotUpdateException(String resourceName, String fieldName, Object filedValue) {
        super(String.format("%s can not edit %s : '%s'", resourceName, fieldName, filedValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.filedValue = filedValue;
    }
}

