package com.supplychain.common.api;

public final class ApiResponseUtils {

    private ApiResponseUtils() {
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.success(data);
    }

    public static ApiResponse<Void> ok() {
        return ApiResponse.success(null);
    }

    public static <T> ApiResponse<T> fail(ApiCode apiCode) {
        return ApiResponse.failure(apiCode, apiCode.message());
    }

    public static <T> ApiResponse<T> fail(ApiCode apiCode, String message) {
        return ApiResponse.failure(apiCode, message);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return ApiResponse.failure(code, message);
    }
}
