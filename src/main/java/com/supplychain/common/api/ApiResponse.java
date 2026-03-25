package com.supplychain.common.api;

import java.time.Instant;

public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
    private long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(ApiCode.SUCCESS.code());
        response.setMessage(ApiCode.SUCCESS.message());
        response.setData(data);
        response.setTimestamp(Instant.now().toEpochMilli());
        return response;
    }

    public static <T> ApiResponse<T> failure(ApiCode apiCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(apiCode.code());
        response.setMessage(message != null ? message : apiCode.message());
        response.setTimestamp(Instant.now().toEpochMilli());
        return response;
    }

    public static <T> ApiResponse<T> failure(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(Instant.now().toEpochMilli());
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
