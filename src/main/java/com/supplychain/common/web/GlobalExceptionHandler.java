package com.supplychain.common.web;

import com.supplychain.common.api.ApiCode;
import com.supplychain.common.api.ApiResponse;
import com.supplychain.common.api.ApiResponseUtils;
import com.supplychain.common.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBizException(BizException e) {
        return ApiResponseUtils.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponseUtils.fail(ApiCode.PARAM_INVALID, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return ApiResponseUtils.fail(ApiCode.SYSTEM_ERROR);
    }
}
