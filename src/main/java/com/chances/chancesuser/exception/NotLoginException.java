package com.chances.chancesuser.exception;

import com.chances.chancesuser.base.BaseException;

/**
 * 异常类
 */
public class NotLoginException extends BaseException {

    public static final String BE_MESSAGE = "未登录";
    public static final Integer CODE = 101;

    public NotLoginException() {
        super(BE_MESSAGE);
        super.setCODE(NotLoginException.CODE);
    }

    @Override
    public Integer getCODE() {
        return super.getCODE();
    }
}
