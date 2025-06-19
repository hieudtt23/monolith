package com.danghieu99.monolith.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotAvailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotAvailableException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not available with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
