package com.roshan.know_base.app.exception;

import com.roshan.know_base.common.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ProblemDetail handleBaseException(BaseException ex, HttpServletRequest request){
        return buildProblemDetail(ex.getStatus(),
                ex.getMessage(),
                ex.getErrorCode().name(),
                request.getRequestURI());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request){
        return buildProblemDetail(HttpStatus.CONFLICT,
                "Database constraint violation.",
                HttpStatus.CONFLICT.name(),
                request.getRequestURI());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildProblemDetail(HttpStatus.CONFLICT,
                message,
                HttpStatus.CONFLICT.name(),
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request){
        log.error("Unhandled exception occurred", ex);
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong.",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                request.getRequestURI());
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            String detail,
            String errorCode,
            String path
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(getTitle(status));
        problemDetail.setProperty("errorCode", errorCode);
        problemDetail.setInstance(URI.create(path));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    private String getTitle(HttpStatus status){
        return switch (status){
            case BAD_REQUEST -> "Validation Error";
            case CONFLICT -> "Data Conflict";
            case NOT_FOUND -> "Resource Not Found";
            case INTERNAL_SERVER_ERROR -> "Internal Server Error";
            case UNPROCESSABLE_CONTENT -> "File Processing Error";
            default -> "Application Error";

        };
    }
}
