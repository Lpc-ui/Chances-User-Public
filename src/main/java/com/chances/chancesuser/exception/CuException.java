package com.chances.chancesuser.exception;

import com.chances.chancesuser.base.BaseException;

/**
 * 异常类
 */
public class CuException extends BaseException {

    public static final String BE_MESSAGE = "业务异常";


    public static final Integer CODE = 500;

    public CuException() {
        super(BE_MESSAGE);
        super.setCODE(CuException.CODE);
    }

    public CuException(String msg) {
        super(BE_MESSAGE);
        super.setCODE(CuException.CODE);
    }

    @Override
    public Integer getCODE() {
        return super.getCODE();
    }
}
