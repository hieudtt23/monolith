package com.danghieu99.monolith.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * <code>422</code>
 * <p>the server understands the content type of the request entity </p>
 * <p>but was unable to process the contained instructions.</p>
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class EntityExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String resourceName;
    private final String fieldName;
    private final Object filedValue;

    public EntityExistsException(String resourceName, String fieldName, Object filedValue) {
        super(String.format("%s is existed with %s : '%s'", resourceName, fieldName, filedValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.filedValue = filedValue;
    }
}
