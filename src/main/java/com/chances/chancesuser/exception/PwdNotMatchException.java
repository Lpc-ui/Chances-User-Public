package com.chances.chancesuser.exception;

/**
 * 异常类
 */
public class PwdNotMatchException extends RuntimeException {

    public static final String BE_MESSAGE = "用户名或密码错误";

    public PwdNotMatchException() {
        super(BE_MESSAGE);
    }

    public PwdNotMatchException(String msg) {
        super(msg);
    }
}
