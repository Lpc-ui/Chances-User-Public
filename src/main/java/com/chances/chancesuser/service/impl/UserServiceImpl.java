package com.chances.chancesuser.service.impl;
import java.time.LocalDateTime;

import com.chances.chancesuser.dao.UserDao;
import com.chances.chancesuser.model.UserMO;
import com.chances.chancesuser.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
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
    public void add() {

    }
}
