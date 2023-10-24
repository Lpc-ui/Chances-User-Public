package com.chances.chancesuser.service.impl;

import com.chances.chancesuser.cuenum.UserAdminCode;
import com.chances.chancesuser.cuenum.UserStatusCode;
import com.chances.chancesuser.dao.UserDao;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.exception.CuException;
import com.chances.chancesuser.model.UserMO;
import com.chances.chancesuser.service.UserService;
import com.chances.chancesuser.utils.BeanCopyUtil;
import com.chances.chancesuser.utils.JwtUtils;
import com.chances.chancesuser.utils.PasswordUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private JwtUtils jwtUtils;
    //初始密码
    @Value("${chances.default-password:#{'qwerty'}}")
    private String defaultPassword;

    @Override
    public void test() {
        UserMO entity = new UserMO();
        entity.setLoginName("李鹏成");
        entity.setPassword("12345");
        entity.setMobile("18286703704");
        entity.setEmail("lpcxyxy@gmail.com");
        entity.setAvata("qqq");
        entity.setAdmin(0);
        entity.setStatus(0);
        entity.setLastLoginTime(LocalDateTime.now());
        userDao.insert(entity);
    }

    @Override
    public void add(UserDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getLoginName())) throw new CuException("用户名不能为空");
        if (StringUtils.isEmpty(userDTO.getMobile())) throw new CuException("手机号不能为空");
        if (StringUtils.isEmpty(userDTO.getEmail())) throw new CuException("邮箱不能为空");
        if (ObjectUtils.isEmpty(userDTO.getAdmin())) userDTO.setAdmin(UserAdminCode.NO_ADMIN.getCode());
        if (ObjectUtils.isEmpty(userDTO.getStatus())) userDTO.setStatus(UserStatusCode.OK.code());
        //初始密码
        userDTO.setLastLoginTime(LocalDateTime.now());
        userDTO.setPassword(PasswordUtils.encodePassword(defaultPassword));
        UserMO userMO = BeanCopyUtil.copyBeanProperties(userDTO, UserMO::new);
        userDao.insert(userMO);
    }

    @Override
    public String userLogin(String loginName, String password) {
        UserMO userMO = userDao.findByLoginName(loginName);
        if (ObjectUtils.isEmpty(userMO)) throw new CuException("用户名或密码错误");
        if (!PasswordUtils.matchPassword(password, userMO.getPassword())) throw new CuException("用户名或密码错误");
        if (userMO.getStatus().equals(UserStatusCode.LOCK.code()))
            throw new CuException(UserStatusCode.LOCK.describe());
        if (userMO.getStatus().equals(UserStatusCode.DISABLE.code()))
            throw new CuException(UserStatusCode.DISABLE.describe());
        return jwtUtils.generateToken(loginName);
    }

    @Override
    public void logout(String token) {
        String userName = jwtUtils.getUsernameFromToken(token);
        if (StringUtils.isEmpty(userName)) return;
        jwtUtils.invalidateToken(userName);
    }
}
