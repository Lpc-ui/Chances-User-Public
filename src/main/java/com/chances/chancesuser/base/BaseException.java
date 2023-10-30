package com.chances.chancesuser.base;

/**
 * 公共异常
 */
public class BaseException extends RuntimeException {
    public Integer CODE = 10000;

    public BaseException() {
        super();
    }

    public BaseException(String msg) {
        super(msg);
    }

    public void setCODE(Integer CODE) {
        this.CODE = CODE;
    }

    public Integer getCODE() {
        return CODE;
    }
}
