package com.chances.chancesuser.service;

import com.chances.chancesuser.base.BaseService;
import com.chances.chancesuser.base.PageJson;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.model.User;

import java.io.InputStream;

public interface UserService extends BaseService {


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
    void logout(String token) throws Exception;

    /**
     * findByName
     *
     * @param userName userName
     * @return UserMO
     */
    User findByName(String userName);

    /**
     * 用户列表
     *
     * @param email    email
     * @param mobile   mobile
     * @param pageSize pageSize
     * @param pageNum  pageNum
     */
    PageJson<UserDTO> userList(String email, String mobile, String pageNum, String pageSize) throws Exception;

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    void userDelete(String userId);

    /**
     * 用户信息
     *
     * @param userId 用户ID
     * @param token  当前登录用户[userID为传递0]
     */
    UserDTO userInfo(String userId, String token);

    /**
     * 更新用户信息
     *
     * @param userId  用户ID
     * @param userDTO 用户信息     | status | | admin |  | email |  | mobile |
     * @param token   登录用户
     */
    void userUpdate(String userId, UserDTO userDTO, String token);

    /**
     * 用户锁定与解锁
     *
     * @param userId 用户
     * @param status 状态
     */
    void lock(String userId, String status);

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param token       当前用户
     */
    void password(String oldPassword, String newPassword, String token) throws Exception;


    /**
     * 设置用户头像路径
     */
//    Result setImage(MultipartFile file, String token);

    /**
     * 图片上传
     * @param inputStream 流
     * @param token token
     */
    void setImage2(InputStream inputStream, String token);
}
