package com.chances.chancesuser.service;

import com.chances.chancesuser.base.BaseService;
import com.chances.chancesuser.dto.UserDTO;

public interface UserService extends BaseService {

    void test();

    void add(UserDTO userDTO);


    /**
     * @param loginName 用户名
     * @param password  密码
     * @return token
     */
    String userLogin(String loginName, String password);
}
