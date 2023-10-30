package com.chances.chancesuser.base;

/**
 * 公共服务层
 */
public interface BaseService {
    /**
     * 获取系统默认的日期格式
     */
    default String datePatternDefault() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
