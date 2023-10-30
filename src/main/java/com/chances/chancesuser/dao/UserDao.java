package com.chances.chancesuser.dao;


import com.chances.chancesuser.base.BaseDao;
import com.chances.chancesuser.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * 用户持久
 */
public interface UserDao extends BaseDao<User> {
    User findByLoginName(String loginName);

    boolean existsByMobile(String mobile);

    boolean existsByLoginName(String loginName);

    /**
     * 更新密码
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);


    /**
     * 更新用户头像路径
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avata = :avata WHERE u.loginName = :loginName")
    void updateAvataByLoginName(@Param("loginName") String loginName, @Param("avata") String avata);

    /**
     * 更新登录时间
     *
     * @param id            用户id
     * @param lastLoginTime 当前时间
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginTime = :lastLoginTime WHERE u.id = :id")
    void updateLastLoginTimeById(@Param("id") Long id, @Param("lastLoginTime") LocalDateTime lastLoginTime);


    /**
     * 更新状态
     *
     * @param id     用户id
     * @param status 状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") Integer status);


    /**
     * 条件分页
     */
    default Page<User> findByEmailAndMobile(String email, String mobile, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("mobile", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<User> example = Example.of(new User(mobile, email), matcher);
        return findAll(example, pageable);
    }
}
