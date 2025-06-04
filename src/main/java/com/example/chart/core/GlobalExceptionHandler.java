package com.example.chart.core;

import com.example.chart.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Custom Business Logic Exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {
        log.warn("Bad request: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflictException(
            ConflictException ex, HttpServletRequest request) {
        log.warn("Conflict: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficientStockException(
            InsufficientStockException ex, HttpServletRequest request) {
        log.warn("Insufficient stock: {} at {}", ex.getMessage(), request.getRequestURI());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("productId", ex.getProductId());
        errorDetails.put("requestedQuantity", ex.getRequestedQuantity());
        errorDetails.put("availableQuantity", ex.getAvailableQuantity());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), Collections.singletonList(ex.getMessage())));
    }

    // Authentication & Authorization Exceptions
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            Exception ex, HttpServletRequest request) {
        log.warn("Authentication failed: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authentication failed", Collections.singletonList("Invalid credentials")));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Access denied", Collections.singletonList("Insufficient permissions")));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(
            UsernameNotFoundException ex, HttpServletRequest request) {
        log.warn("User not found: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("User not found", Collections.singletonList(ex.getMessage())));
    }

    // JWT Exceptions
    @ExceptionHandler({InvalidTokenException.class, JwtException.class})
    public ResponseEntity<ApiResponse<Object>> handleInvalidTokenException(
            Exception ex, HttpServletRequest request) {
        log.warn("Invalid token: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid token", Collections.singletonList("Token is invalid or malformed")));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredJwtException(
            ExpiredJwtException ex, HttpServletRequest request) {
        log.warn("Expired token: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Token expired", Collections.singletonList("Please login again")));
    }

    // Validation Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed at {}", request.getRequestURI());
        
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("Type mismatch: {} at {}", ex.getMessage(), request.getRequestURI());
        String error = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid parameter type", Collections.singletonList(error)));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("Missing parameter: {} at {}", ex.getMessage(), request.getRequestURI());
        String error = String.format("Missing required parameter: %s", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Missing required parameter", Collections.singletonList(error)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Invalid JSON: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid JSON format", Collections.singletonList("Request body is not valid JSON")));
    }

    // Database Exceptions
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Data integrity violation: {} at {}", ex.getMessage(), request.getRequestURI());
        
        String message = "Data integrity violation";
        if (ex.getMessage().contains("unique")) {
            message = "Resource already exists";
        } else if (ex.getMessage().contains("foreign key")) {
            message = "Referenced resource does not exist";
        } else if (ex.getMessage().contains("not null")) {
            message = "Required field is missing";
        }
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message, Collections.singletonList("Please check your data and try again")));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Entity not found: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Entity not found", Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<Object>> handleSQLException(
            SQLException ex, HttpServletRequest request) {
        log.error("Database error: {} at {}", ex.getMessage(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Database error", Collections.singletonList("A database error occurred")));
    }

    // HTTP Method Exceptions
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("Method not supported: {} at {}", ex.getMessage(), request.getRequestURI());
        String error = String.format("Method %s not supported for this endpoint", ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error("Method not allowed", Collections.singletonList(error)));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.warn("Media type not supported: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ApiResponse.error("Unsupported media type", Collections.singletonList("Content-Type not supported")));
    }

    // File Upload Exceptions
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("File size exceeded: {} at {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ApiResponse.error("File too large", Collections.singletonList("Maximum file size exceeded")));
    }

    // Generic Exception Handler (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {} at {}", ex.getMessage(), request.getRequestURI(), ex);
        
        // Don't expose internal error details in production
        String message = "An unexpected error occurred";
        List<String> errors = Collections.singletonList("Please contact support if the problem persists");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(message, errors));
    }
}
