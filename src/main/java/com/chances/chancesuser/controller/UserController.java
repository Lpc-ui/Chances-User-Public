package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.R;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return R.ok().put("token", userService.userLogin(loginName, password));
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
    private R logout(@RequestHeader String token) throws Exception {
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
        return R.ok(userService.userList(email, mobile, pageNum, pageSize)).setMsg("条件分页");
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @DeleteMapping(value = "/user/{userId}")
    private R userDelete(@PathVariable String userId) {
        userService.userDelete(userId);
        return R.ok().setMsg("移除成功");
    }


    /**
     * 用户信息
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/user/{userId}")
    private R userInfo(@PathVariable String userId) {
        return R.ok(userService.userInfo(userId)).setMsg("用户信息");
    }

    /**
     * 用户信息修改
     *
     * @param userId 用户id
     */
    @PutMapping(value = "/user/{userId}")
    private R userUpdate(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        userService.userUpdate(userId, userDTO);
        return R.ok().setMsg("修改成功");
    }


    /**
     * 用户锁定与解锁
     *
     * @param userId 用户id
     * @param status 用户状态
     */
    @PostMapping(value = "user/update/status")
    private R lock(String status, String userId) {
        userService.lock(userId, status);
        return R.ok().setMsg("更新成功");
    }


    /**
     * 用户修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @PostMapping(value = "user/update/password")
    private R password(String oldPassword, String newPassword, @RequestHeader String token) throws Exception {
        userService.password(oldPassword, newPassword, token);
        return R.ok().setMsg("更新成功");
    }

    /**
     * 用户修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @PostMapping(value = "user/avatar/upload")
    private R avatarUpload(String oldPassword, String newPassword, @RequestHeader String token) throws Exception {
        userService.password(oldPassword, newPassword, token);
        return R.ok().setMsg("更新成功");
    }

    /**
     * 图片上传
     *
     * @param file  文件
     * @param token 用户
     */
    @PostMapping("/user/avatar/upload")
    @ResponseBody
    public R uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader String token) {
        return userService.setImage(file, token);
    }
}
