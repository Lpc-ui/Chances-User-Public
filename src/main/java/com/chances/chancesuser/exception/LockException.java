package com.chances.chancesuser.exception;

/**
 * 异常类
 */
public class LockException extends RuntimeException {

    public static final String BE_MESSAGE = "锁定异常";

    public LockException() {
        super(BE_MESSAGE);
    }

    public LockException(String msg) {
        super(msg);
    }
}
