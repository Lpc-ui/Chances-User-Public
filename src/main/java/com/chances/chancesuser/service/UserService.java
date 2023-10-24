package com.chances.chancesuser.service;

import com.chances.chancesuser.base.BaseService;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.model.UserMO;

public interface UserService extends BaseService {

    void test();

    /**
     * 增加用户
     *
     * @param userDTO userDTO
     */
    void add(UserDTO userDTO);


    /**
     * @param loginName 用户名
     * @param password  密码
     * @return token
     */
    String userLogin(String loginName, String password);

    /**
     * 用户退出
     *
     * @param token token
     */
    void logout(String token);

    /**
     * findByName
     *
     * @param userName userName
     * @return UserMO
     */
    UserMO findByName(String userName);
}
