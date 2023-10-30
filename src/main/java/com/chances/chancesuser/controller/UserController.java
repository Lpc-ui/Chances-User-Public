package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.Result;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginName 用户名
     * @param password  密码
     * @return token
     */
    @PostMapping(value = "/login")
    private Result userLogin(@RequestParam("loginName") String loginName,
                             @RequestParam("password") String password) {
        String token = userService.userLogin(loginName, password);
        return Result.ok().put("token", token).setMsg("登录成功");
    }


    /**
     * 新增用户
     *
     * @param userDTO userDTO
     * @return R
     */
    @PostMapping(value = "/add")
    private Result userAdd(@RequestBody UserDTO userDTO) {
        userService.add(userDTO);
        return Result.ok().setMsg("创建成功");
    }


    /**
     * 用户退出
     *
     * @param token token
     * @return ok
     */
    @GetMapping(value = "/logout")
    private Result logout(@RequestHeader String token) throws Exception {
        userService.logout(token);
        return Result.ok().setMsg("退出成功");
    }

    /**
     * 用户列表接口
     *
     * @param email    email
     * @param mobile   mobile
     * @param pageSize pageSize
     * @param pageNum  pageNum
     */
    @GetMapping(value = "/list")
    private Result userList(@RequestParam(required = false) String email,
                            @RequestParam(required = false) String mobile,
                            @RequestParam(required = false, defaultValue = "10") String pageSize,
                            @RequestParam(required = false, defaultValue = "1") String pageNum) throws Exception {
        return Result.ok(userService.userList(email, mobile, pageNum, pageSize)).setMsg("条件分页");
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @DeleteMapping(value = "/{userId}")
    private Result userDelete(@PathVariable Long userId) {
        userService.userDelete(userId);
        return Result.ok().setMsg("移除成功");
    }


    /**
     * 用户信息
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/{userId}")
    private Result userInfo(@PathVariable Long userId,
                            @RequestHeader String token) {
        return Result.ok(userService.userInfo(userId, token)).setMsg("用户信息");
    }

    /**
     * 用户信息修改
     *
     * @param userId 用户id
     */
    @PutMapping(value = "/{userId}")
    private Result userUpdate(@PathVariable Long userId,
                              @RequestBody UserDTO userDTO,
                              @RequestHeader String token) {
        userService.userUpdate(userId, userDTO, token);
        return Result.ok().setMsg("修改成功");
    }


    /**
     * 用户锁定与解锁
     *
     * @param userId 用户id
     * @param status 用户状态
     */
    @PostMapping(value = "/update/status")
    private Result lock(@RequestParam("status") String status,
                        @RequestParam("userId") Long userId) {
        userService.lock(userId, status);
        return Result.ok().setMsg("更新成功");
    }


    /**
     * 用户修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @PostMapping(value = "/update/password")
    private Result password(@RequestParam("oldPassword") String oldPassword,
                            @RequestParam("newPassword") String newPassword,
                            @RequestHeader String token) throws Exception {
        userService.password(oldPassword, newPassword, token);
        return Result.ok().setMsg("更新成功");
    }

    /**
     * 图片上传
     *
     * @param file  文件
     * @param token 用户
     */
    @PostMapping("/avatar/upload")
    public Result uploadFile(@RequestHeader String token,
                             @RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        userService.setImage2(inputStream, token);
        return Result.ok().setMsg("上传成功");
    }

}
