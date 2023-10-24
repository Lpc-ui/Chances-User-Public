package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.R;
import com.chances.chancesuser.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    /**
     * test
     */
    @RequestMapping(value = "/info", method = {RequestMethod.POST})
    private R test() {
        userService.test();
        return R.ok("123");
    }

    /**
     * test
     */
    @RequestMapping(value = "/user/login", method = {RequestMethod.POST})
    private R userLogin() {
        userService.test();
        return R.ok("123");
    }

    /**
     * 新增用户
     */
    @RequestMapping(value = "/user/add", method = {RequestMethod.POST})
    private R userAdd() {
        userService.add();
        return R.ok("123");
    }
}
