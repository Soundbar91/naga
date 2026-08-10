package io.naga.commerce.global.error;

import org.springframework.validation.FieldError;

public record FieldErrorDetail(
    String field,
    String message
) {
    public static FieldErrorDetail from(FieldError error) {
        return new FieldErrorDetail(error.getField(), error.getDefaultMessage());
    }
}
