package com.chances.chancesuser.config;

import com.chances.chancesuser.base.ErrorCode;
import com.chances.chancesuser.base.Result;
import com.chances.chancesuser.exception.CuException;
import com.chances.chancesuser.exception.LockException;
import com.chances.chancesuser.exception.NotLoginException;
import com.chances.chancesuser.exception.PwdNotMatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


/**
 * 全局异常处理[过滤器的错误获取不到]
 */
@RestControllerAdvice
public class GlobalExceptionConfig {
    // 拦截：未登录异常
    @ExceptionHandler(CuException.class)
    public Result handlerException(CuException e) {
        // 打印堆栈，以供调试
        e.printStackTrace();
        // 返回给前端
        return Result.failed(new CuException(e.getMessage()));
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        e.printStackTrace();
        return Result.failed(ErrorCode.SYS_UNKNOWN, "内部错误");
    }

    // 拦截：未登录异常
    @ExceptionHandler(NotLoginException.class)
    public Result handlerException(NotLoginException e) {
        e.printStackTrace();
        return Result.failed(new NotLoginException());
    }

    // 拦截：锁定异常
    @ExceptionHandler(LockException.class)
    public Result handlerException(LockException e) {
        e.printStackTrace();
        return Result.failed(new LockException());
    }

    // 拦截：密码异常
    @ExceptionHandler(PwdNotMatchException.class)
    public Result handlerException(PwdNotMatchException e) {
        e.printStackTrace();
        return Result.failed(new PwdNotMatchException());
    }

    // 拦截：文件过大
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handlerException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return Result.failed("文件过大");
    }



}
