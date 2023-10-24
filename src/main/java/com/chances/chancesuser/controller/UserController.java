package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.R;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 用户登录
     */
    @RequestMapping(value = "/user/login", method = {RequestMethod.POST})
    private R userLogin(String loginName, String password) {
        String token = userService.userLogin(loginName, password);
        return R.ok().put("token", token);
    }


    /**
     * 新增用户
     *
     * @param userDTO userDTO
     * @return R
     */
    @RequestMapping(value = "/user/add", method = {RequestMethod.POST})
    private R userAdd(@RequestBody UserDTO userDTO) {
        userService.add(userDTO);
        return R.ok().setMsg("创建成功");
    }
}
