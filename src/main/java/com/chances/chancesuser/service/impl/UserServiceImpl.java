package com.chances.chancesuser.service.impl;

import com.chances.chancesuser.base.PageJson;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
        String token = jwtUtils.generateToken(loginName);
        userDao.updateLastLoginTimeById(userMO.getId(), LocalDateTime.now());
        return token;
    }

    @Override
    public void logout(String token) throws Exception {
        String userName = jwtUtils.getUsernameFromToken(token);
        if (StringUtils.isEmpty(userName)) return;
        jwtUtils.invalidateToken(userName);
    }

    @Override
    public UserMO findByName(String userName) {
        return userDao.findByLoginName(userName);
    }

    @Override
    public PageJson<UserMO> userList(String email, String mobile, String pageNum, String pageSize) {
        int size = Integer.parseInt(pageSize);
        int num = Integer.parseInt(pageNum) - 1;
        PageRequest pageRequest = PageRequest.of(num, size);
        Page<UserMO> pageBo = userDao.findByEmailAndMobile(email, mobile, pageRequest);
        PageJson<UserMO> data = new PageJson<>();
        data.setPageSize(size);
        data.setPageNum(num);
        data.setPageTotal(pageBo.getTotalPages());
        data.setDataTotal(pageBo.getTotalElements());
        List<UserMO> content = pageBo.getContent();
        data.setJsonList(content);
        return data;
    }

    @Override
    public void userDelete(String userId) {
        userDao.deleteById(Long.valueOf(userId));
    }

    @Override
    public UserDTO userInfo(String userId) {
        UserMO userMO = userDao.findById(Long.valueOf(userId)).orElse(null);
        UserDTO userDTO = BeanCopyUtil.copyBeanProperties(userMO, UserDTO::new);
        userDTO.setAvata(null);
        userDTO.setStatus(null);
        userDTO.setPassword(null);
        userDTO.setLastLoginTime(null);
        return userDTO;
    }

    @Override
    public void userUpdate(String userId, UserDTO userDTO) {
        UserMO userMO = userDao.findById(Long.valueOf(userId)).orElse(null);
        if (userMO == null) throw new CuException("参数有误");
        if (ObjectUtils.isNotEmpty(userDTO.getStatus())) userMO.setStatus(userDTO.getStatus());
        if (ObjectUtils.isNotEmpty(userDTO.getAdmin())) userMO.setAdmin(userDTO.getAdmin());
        if (ObjectUtils.isNotEmpty(userDTO.getEmail())) userMO.setEmail(userDTO.getEmail());
        if (ObjectUtils.isNotEmpty(userDTO.getMobile())) userMO.setMobile(userDTO.getMobile());
        //手机号校验
        if (ObjectUtils.isNotEmpty(userDTO.getMobile()) && userDao.existsByMobile(userDTO.getMobile()))
            throw new CuException("手机号重复");
        userDao.save(userMO);
    }

    @Override
    public void lock(String userId, String status) {
        userDao.updateStatusById(Long.parseLong(userId), Integer.parseInt(status));
    }

    @Override
    public void password(String oldPassword, String newPassword, String token) throws Exception {
        String username = jwtUtils.getUsernameFromToken(token);
        UserMO userMO = userDao.findByLoginName(username);
        if (!PasswordUtils.matchPassword(oldPassword, userMO.getPassword())) throw new CuException("原密码有误");
        if (StringUtils.isEmpty(newPassword)) throw new CuException("请输入新密码");
        userDao.updatePasswordById(userMO.getId(), PasswordUtils.encodePassword(newPassword));
        //移除token[退出登录]
        jwtUtils.invalidateToken(username);
    }

    @Override
    public void setImage(String username, String avata) {
        userDao.updateAvataByLoginName(username, avata);
    }

}
