package com.chances.chancesuser.dao;


import com.chances.chancesuser.base.BaseDao;
import com.chances.chancesuser.model.UserMO;

public interface UserDao extends BaseDao<UserMO> {
    UserMO findByLoginName(String loginName);
}
