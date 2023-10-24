package com.chances.chancesuser.config;

import com.chances.chancesuser.base.ErrorCode;
import com.chances.chancesuser.base.R;
import com.chances.chancesuser.exception.CuException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionConfig {
    // 拦截：未登录异常
    @ExceptionHandler(CuException.class)
    public R handlerException(CuException e) {
        // 打印堆栈，以供调试
        e.printStackTrace();
        // 返回给前端
        return R.failed(ErrorCode.CU_EX, e.getMessage());
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e) {
        e.printStackTrace();
        return R.failed(ErrorCode.SYS_UNKNOWN, "内部错误");
    }

}
