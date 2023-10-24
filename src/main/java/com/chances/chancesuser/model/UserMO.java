package com.chances.chancesuser.model;

import com.chances.chancesuser.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 用户表实体
 */
@Entity
@Data
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
public class UserMO extends BaseModel {
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像地址
     */
    private String avata;
    /**
     * 管理员状态
     */
    private Integer admin;
    /**
     * 用户状态
     */
    private Integer status;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}
