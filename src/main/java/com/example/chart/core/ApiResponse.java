package com.example.chart.core;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private List<String> errors;

    // Success response
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("SUCCESS");
        response.setMessage(message);
        response.setData(data);
        response.setErrors(null);
        return response;
    }

    // Error response
    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("ERROR");
        response.setMessage(message);
        response.setData(null);
        response.setErrors(errors);
        return response;
    }
}
