package com.chances.chancesuser.base;

/**
 * 消息状态码
 */
public enum ErrorCode {
    SYS_UNKNOWN(110, "系统未知错误"),

    SYS_DB(111, "数据库异常"),

    SYS_LOGIN_NO(101, "用户未登录"),

    SYS_NO_PERMISSION(100, "用户禁用");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;

    private final String message;

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}