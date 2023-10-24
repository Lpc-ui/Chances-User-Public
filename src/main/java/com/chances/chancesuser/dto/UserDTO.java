package com.chances.chancesuser.dto;

import com.chances.chancesuser.base.PageBo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
//序列化时候只包含字段不为null的字段
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends PageBo {
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
    @JsonIgnore
    private LocalDateTime lastLoginTime;
}
