package com.chances.chancesuser.exception;

import com.chances.chancesuser.base.BaseException;

/**
 * 异常类
 */
public class LockException extends BaseException {

    public static final String BE_MESSAGE = "用户被锁定";
    public static final Integer CODE = 100;

    public LockException() {
        super(BE_MESSAGE);
        super.setCODE(LockException.CODE);
    }

    @Override
    public Integer getCODE() {
        return super.getCODE();
    }
}
