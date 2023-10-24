package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.PageJson;
import com.chances.chancesuser.base.R;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.model.UserMO;
import com.chances.chancesuser.service.UserService;
import org.springframework.web.bind.annotation.*;

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
     *
     * @param loginName 用户名
     * @param password  密码
     * @return token
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


    /**
     * 用户退出
     *
     * @param token token
     * @return ok
     */
    @RequestMapping(value = "/user/login", method = {RequestMethod.GET})
    private R logout(@RequestHeader String token) {
        userService.logout(token);
        return R.ok().setMsg("退出成功");
    }

    /**
     * 用户列表接口
     *
     * @param email    email
     * @param mobile   mobile
     * @param pageSize pageSize
     * @param pageNum  pageNum
     */
    @RequestMapping(value = "/user/list", method = {RequestMethod.GET})
    private R userList(@RequestParam(required = false) String email,
                       @RequestParam(required = false) String mobile,
                       @RequestParam(required = false, defaultValue = "10") String pageSize,
                       @RequestParam(required = false, defaultValue = "1") String pageNum) throws Exception {
        PageJson<UserMO> pageJson = userService.userList(email, mobile, pageNum, pageSize);
        return R.ok(pageJson);
    }
}
