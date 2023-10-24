package com.chances.chancesuser.dao;


import com.chances.chancesuser.base.BaseDao;
import com.chances.chancesuser.model.UserMO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDao extends BaseDao<UserMO> {
    UserMO findByLoginName(String loginName);

    default Page<UserMO> findByEmailAndMobile(String email, String mobile, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("mobile", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<UserMO> example = Example.of(new UserMO(mobile,email), matcher);
        return findAll(example, pageable);
    }
}
