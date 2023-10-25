package com.chances.chancesuser.base;

import com.chances.chancesuser.exception.CuException;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回类
 */
@SuppressWarnings("all")
public class Result<T> implements Serializable {
    /**
     * 返回码:0-成功 大于0失败
     */
    private int status = 0;
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
    public static Result ok(Object data) {
        Result<Object> objectResult = new Result<>(data);
        objectResult.setCode(200);
        return objectResult;
    }

    public static Result<Map<String, Object>> ok() {
        Result<Map<String, Object>> objectResult = new Result<>(new HashMap<>());
        objectResult.setCode(200);
        return objectResult;
    }

    public Result put(String key, Object value) {
        ((HashMap<String, Object>) this.data).put(key, value);
        return this;
    }

    /**
     * 失败
     */
    public static Result failed(ErrorCode errorCode) {
        Result info = new Result();
        info.setCode(errorCode.getCode());
        info.setMsg(errorCode.getMessage());
        return info;
    }

    /**
     * 失败-自定义错误信息
     */
    public static Result failed(ErrorCode errorCode, String msg) {
        Result info = new Result();
        info.setCode(errorCode.getCode());
        if (StringUtils.isEmpty(msg)) {
            info.setMsg(errorCode.getMessage());
        } else {
            info.setMsg(msg);
        }
        return info;
    }

    public static Result failed(BaseException exception) {
        Result info = new Result();
        info.setCode(exception.getCODE());
        info.setMsg(exception.getMessage());
        return info;
    }

    public static Result failed(String msg) {
        Result info = new Result();
        info.setCode(CuException.CODE);
        info.setMsg(msg);
        return info;
    }

    public Result() {
    }

    private Result(T data) {
        this.data = data;
        this.msg = "success";
    }

    /**
     * 添加错误码
     */
    public void setError(ErrorCode errorCode) {
        this.status = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }

    /**
     * 添加错误码-消息自定义
     */
    public void setError(ErrorCode errorCode, String message) {
        this.status = errorCode.getCode();
        this.msg = message;
    }

    public int getCode() {
        return this.status;
    }

    public void setCode(int code) {
        this.status = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
