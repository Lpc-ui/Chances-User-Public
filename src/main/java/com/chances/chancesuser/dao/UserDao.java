package com.chances.chancesuser.dao;


import com.chances.chancesuser.base.BaseDao;
import com.chances.chancesuser.model.UserMO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserDao extends BaseDao<UserMO> {
    UserMO findByLoginName(String loginName);


    /**
     * 更新登录时间
     *
     * @param id            用户id
     * @param lastLoginTime 当前时间
     */
    @Modifying
    @Query("UPDATE UserMO u SET u.lastLoginTime = :lastLoginTime WHERE u.id = :id")
    int updateLastLoginTimeById(@Param("id") Long id, @Param("lastLoginTime") LocalDateTime lastLoginTime);


    /**
     * 条件分页
     */
    default Page<UserMO> findByEmailAndMobile(String email, String mobile, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("mobile", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<UserMO> example = Example.of(new UserMO(mobile, email), matcher);
        return findAll(example, pageable);
    }
}
