package com.student.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理参数验证异常（@RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数验证失败：{}", errors);
        return ApiResponse.error(400, "参数验证失败：" + errors);
    }

    /**
     * 处理参数验证异常（@ModelAttribute）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBindException(BindException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warn("参数绑定失败：{}", errors);
        return ApiResponse.error(400, "参数绑定失败：" + errors);
    }

    /**
     * 处理单个参数验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        logger.warn("约束验证失败：{}", errors);
        return ApiResponse.error(400, "约束验证失败：" + errors);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("参数错误：{}", e.getMessage());
        return ApiResponse.error(400, e.getMessage());
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleIllegalStateException(IllegalStateException e) {
        logger.warn("状态错误：{}", e.getMessage());
        return ApiResponse.error(404, e.getMessage());
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        logger.error("服务器内部错误", e);
        return ApiResponse.error(500, "服务器内部错误：" + e.getMessage());
    }
}
