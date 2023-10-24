package com.chances.chancesuser.cuenum;

/**
 * 管理员状态码
 */
public enum UserStatusCode {
    DISABLE(0, "禁用"),
    OK(1, "正常"),
    LOCK(2, "锁定");

    UserStatusCode(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    private final int code;

    private final String describe;

    public String getdescribe() {
        return this.describe;
    }

    public int getCode() {
        return this.code;
    }
}