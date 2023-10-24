package com.chances.chancesuser.exception;

/**
 * 异常类
 */
public class CuException extends RuntimeException {

    public static final String BE_MESSAGE = "业务异常";

    public CuException() {
        super(BE_MESSAGE);
    }

    public CuException(String msg) {
        super(msg);
    }
}
