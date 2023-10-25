package com.chances.chancesuser.exception;

import com.chances.chancesuser.base.BaseException;

/**
 * 异常类
 */
public class PwdNotMatchException extends BaseException {
    public static final String BE_MESSAGE = "用户名或密码错误";
    public static final Integer CODE = 103;

    public PwdNotMatchException() {
        super(BE_MESSAGE);
        super.setCODE(PwdNotMatchException.CODE);
    }

    @Override
    public Integer getCODE() {
        return super.getCODE();
    }
}
