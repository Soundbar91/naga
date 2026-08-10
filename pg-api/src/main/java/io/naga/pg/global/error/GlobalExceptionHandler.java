package io.naga.pg.global.error;

import static io.naga.pg.global.error.ErrorCode.BAD_REQUEST;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.naga.pg.global.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.error(errorCode, exception.getDetail()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        List<FieldErrorDetail> fieldErrors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldErrorDetail::from)
            .toList();

        return ResponseEntity
            .status(BAD_REQUEST.getStatus())
            .body(ApiResponse.error(BAD_REQUEST, fieldErrors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException exception
    ) {
        return ResponseEntity
            .status(BAD_REQUEST.getStatus())
            .body(ApiResponse.error(BAD_REQUEST, "parameter : " + exception.getName()));
    }
}
