package com.chances.chancesuser.base;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 通用返回类
 */
public class R<T> implements Serializable {
    /**
     * 返回码:0-成功 大于0失败
     */
    private int code = 0;
    /**
     * 消息提示
     */
    private String msg;
    /**
     * 数据对象
     */
    private T data;
    /**
     * 成功:Str返回,有返回code 200
     */
    public static R ok(Object data) {
        R<Object> objectR = new R<>(data);
        objectR.setCode(200);
        return objectR;
    }

    /**
     * 失败
     */
    public static R failed(ErrorCode errorCode) {
        R info = new R();
        info.setCode(errorCode.getCode());
        info.setMsg(errorCode.getMessage());
        return info;
    }

    /**
     * 失败-自定义错误信息
     */
    public static R failed(ErrorCode errorCode, String msg) {
        R info = new R();
        info.setCode(errorCode.getCode());
        if (StringUtils.isEmpty(msg)) {
            info.setMsg(errorCode.getMessage());
        } else {
            info.setMsg(msg);
        }
        return info;
    }

    public R() {
    }

    private R(T data) {
        this.data = data;
        this.msg = "success";
    }

    /**
     * 添加错误码
     */
    public void setError(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }

    /**
     * 添加错误码-消息自定义
     */
    public void setError(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.msg = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
