package com.supplychain.common.api;

public enum ApiCode {
    SUCCESS("0", "OK"),
    PARAM_INVALID("PARAM_001", "Invalid request parameter"),
    BIZ_NOT_FOUND("BIZ_404", "Resource not found"),
    SYSTEM_ERROR("SYS_500", "Internal Server Error");

    private final String code;
    private final String message;

    ApiCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
