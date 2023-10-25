package com.chances.chancesuser.exception;

/**
 * 异常类
 */
public class NotLoginException extends RuntimeException {

    public static final String BE_MESSAGE = "未登录异常";

    public NotLoginException() {
        super(BE_MESSAGE);
    }

    public NotLoginException(String msg) {
        super(msg);
    }
}
