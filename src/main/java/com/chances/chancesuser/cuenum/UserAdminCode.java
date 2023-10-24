package com.chances.chancesuser.cuenum;

/**
 * 管理员状态码
 */
public enum UserAdminCode {
    IS_ADMIN(1, "管理员"),

    NO_ADMIN(0, "非管理员");

    UserAdminCode(int code, String describe) {
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