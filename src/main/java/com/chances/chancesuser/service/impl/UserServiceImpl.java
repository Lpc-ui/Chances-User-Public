package com.chances.chancesuser.service.impl;

import com.chances.chancesuser.base.ErrorCode;
import com.chances.chancesuser.base.PageJson;
import com.chances.chancesuser.base.Result;
import com.chances.chancesuser.base.StaticPro;
import com.chances.chancesuser.cuenum.UserAdminCode;
import com.chances.chancesuser.cuenum.UserStatusCode;
import com.chances.chancesuser.dao.UserDao;
import com.chances.chancesuser.dto.UserDTO;
import com.chances.chancesuser.exception.CuException;
import com.chances.chancesuser.exception.LockException;
import com.chances.chancesuser.exception.PwdNotMatchException;
import com.chances.chancesuser.model.User;
import com.chances.chancesuser.service.UserService;
import com.chances.chancesuser.utils.BeanCopyUtil;
import com.chances.chancesuser.utils.JwtUtils;
import com.chances.chancesuser.utils.PasswordUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private JwtUtils jwtUtils;
    //初始密码
    @Value("${chances.default-password:#{'qwerty'}}")
    private String defaultPassword;

    /**
     * 增加用户
     *
     * @param userDTO userDTO
     */
    @Override
    public void add(UserDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getLoginName())) {
            throw new CuException("用户名不能为空");
        }
        if (userDao.existsByLoginName(userDTO.getLoginName())) {
            throw new CuException("该用户名已存在");
        }
        if (StringUtils.isEmpty(userDTO.getMobile())) {
            throw new CuException("手机号不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getEmail())) {
            throw new CuException("邮箱不能为空");
        }
        if (ObjectUtils.isEmpty(userDTO.getAdmin())) {
            userDTO.setAdmin(UserAdminCode.NO_ADMIN.getCode());
        }
        if (ObjectUtils.isEmpty(userDTO.getStatus())) {
            userDTO.setStatus(UserStatusCode.OK.code());
        }
        //初始密码
        userDTO.setLastLoginTime(LocalDateTime.now());
        userDTO.setPassword(PasswordUtils.encodePassword(defaultPassword));
        User user = BeanCopyUtil.copyBeanProperties(userDTO, User::new);
        userDao.insert(user);
    }


    /**
     * @param loginName 用户名
     * @param password  密码
     * @return token
     */
    @Override
    public String userLogin(String loginName, String password) {
        User user = userDao.findByLoginName(loginName);
        if (ObjectUtils.isEmpty(user)) {
            throw new PwdNotMatchException();
        }
        if (!PasswordUtils.matchPassword(password, user.getPassword())) {
            throw new PwdNotMatchException();
        }
        if (user.getStatus().equals(UserStatusCode.LOCK.code())) {
            throw new LockException();
        }
        if (user.getStatus().equals(UserStatusCode.DISABLE.code())) {
            throw new LockException();
        }
        String token = jwtUtils.generateToken(loginName);
        userDao.updateLastLoginTimeById(user.getId(), LocalDateTime.now());
        return token;
    }

    /**
     * 用户退出
     *
     * @param token token
     */
    @Override
    public void logout(String token) {
        String userName = jwtUtils.getUsernameFromToken(token);
        if (StringUtils.isEmpty(userName)) {
            return;
        }
        jwtUtils.invalidateToken(userName);
    }

    /**
     * findByName
     *
     * @param userName userName
     * @return UserMO
     */
    @Override
    public User findByName(String userName) {
        return userDao.findByLoginName(userName);
    }

    /**
     * 用户列表
     *
     * @param email    email
     * @param mobile   mobile
     * @param pageSize pageSize
     * @param pageNum  pageNum
     */
    @Override
    public PageJson<UserDTO> userList(String email, String mobile, String pageNum, String pageSize) {
        int size = Integer.parseInt(pageSize);
        int num = Integer.parseInt(pageNum) - 1;
        PageRequest pageRequest = PageRequest.of(num, size, Sort.by("lastLoginTime").descending());
        Page<User> pageBo = userDao.findByEmailAndMobile(email, mobile, pageRequest);
        PageJson<UserDTO> data = new PageJson<>();
        data.setPageSize(size);
        data.setPageNum(Integer.valueOf(pageNum));
        data.setPageTotal(pageBo.getTotalPages());
        data.setDataTotal(pageBo.getTotalElements());
        List<User> content = pageBo.getContent();
        List<UserDTO> userDTOS = BeanCopyUtil.copyListProperties(content, UserDTO::new);
        List<UserDTO> userDTOSNew = userDTOS.stream()
                .peek(userDTO -> userDTO.setAvata(null))
                .peek(userDTO -> userDTO.setPassword(null))
                .collect(Collectors.toList());
        data.setJsonList(userDTOSNew);
        return data;
    }

    /**
     * 删除
     *
     * @param userId 用户ID
     */
    @Override
    public void userDelete(String userId) {
        userDao.deleteById(Long.valueOf(userId));
    }

    /**
     * 用户信息
     *
     * @param userId 用户ID
     * @param token  当前登录用户[userID为传递0]
     */
    @Override
    public UserDTO userInfo(String userId, String token) {
        User user = this.getUser(userId, token);
        UserDTO userDTO = BeanCopyUtil.copyBeanProperties(user, UserDTO::new);
        userDTO.setPassword(null);
        return userDTO;
    }

    /**
     * 更新用户信息
     *
     * @param userId  用户ID
     * @param userDTO 用户信息     | status | | admin |  | email |  | mobile |
     * @param token   登录用户
     */

    @Override
    public void userUpdate(String userId, UserDTO userDTO, String token) {
        User user = this.getUser(userId, token);
        if (user == null) {
            throw new CuException("参数有误");
        }
        if (ObjectUtils.isNotEmpty(userDTO.getStatus())) {
            user.setStatus(userDTO.getStatus());
        }
        if (ObjectUtils.isNotEmpty(userDTO.getAdmin())) {
            user.setAdmin(userDTO.getAdmin());
        }
        if (ObjectUtils.isNotEmpty(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }
        if (ObjectUtils.isNotEmpty(userDTO.getMobile())) {
            user.setMobile(userDTO.getMobile());
        }
        //手机号校验
        boolean b = userDao.existsByMobile(userDTO.getMobile());
        boolean notEmpty = ObjectUtils.isNotEmpty(userDTO.getMobile());
        boolean equals = user.getMobile().equals(userDTO.getMobile());
        if (notEmpty && b && !equals) {
            throw new CuException("手机号重复");
        }
        userDao.save(user);
    }

    /**
     * 获取用户
     *
     * @param userId 用户id
     * @param token  当前token
     */
    private User getUser(String userId, String token) {
        return userId.equals(StaticPro.CUURENT_LOGIN_USER)
                ? userDao.findByLoginName(jwtUtils.getUsernameFromToken(token))
                : userDao.findById(Long.valueOf(userId)).orElse(null);
    }

    /**
     * 用户锁定与解锁
     *
     * @param userId 用户
     * @param status 状态
     */
    @Override
    public void lock(String userId, String status) {
        userDao.updateStatusById(Long.parseLong(userId), Integer.parseInt(status));
    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param token       当前用户
     */
    @Override
    public void password(String oldPassword, String newPassword, String token) throws Exception {
        String username = jwtUtils.getUsernameFromToken(token);
        User user = userDao.findByLoginName(username);
        if (!PasswordUtils.matchPassword(oldPassword, user.getPassword())) {
            throw new CuException("原密码有误");
        }
        if (StringUtils.isEmpty(newPassword)) {
            throw new CuException("请输入新密码");
        }
        userDao.updatePasswordById(user.getId(), PasswordUtils.encodePassword(newPassword));
        //移除token[退出登录]
        jwtUtils.invalidateToken(username);
    }


    /**
     * 上传路径
     */
    @Value("${chances.upload.dir}")
    private String uploadDir;


    /**
     * 可用图片
     */
    private final List<String> images = Arrays.asList(".png", ".jpg", ".jpg", ".jpeg");

    /**
     * 设置图片
     *
     * @param file 文科
     */
    @Override
    public Result setImage(MultipartFile file, String token) {
        try {
            // 构建文件路径
            String username = jwtUtils.getUsernameFromToken(token);
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            if (size > Integer.parseInt(maxFileSize)) {
                throw new CuException("文件过大");
            }
            assert originalFilename != null;
            String fix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!images.contains(fix)) {
                throw new CuException("文件类型有误");
            }
            Path filePath = Paths.get(currentWorkingDir + uploadDir, username + fix);
            // 将文件保存到指定路径
            file.transferTo(filePath.toFile());
            userDao.updateAvataByLoginName(username, username + fix);
            return Result.ok().setMsg("上传成功");
        } catch (IOException e) {
            return Result.failed(ErrorCode.CU_EX).setMsg("上传失败");
        }
    }

    @Value("${user.dir}")
    private String currentWorkingDir;

    @Value("${chances.max-file-size}")
    private String maxFileSize;

    /**
     * 图片上传
     *
     * @param inputStream 流
     * @param token       token
     */
    @Override
    public void setImage2(InputStream inputStream, String token) {
        try {
            // 从token获取用户名
            String username = jwtUtils.getUsernameFromToken(token);
            // 用于此示例的假设文件名和类型
            String originalFilename = username + ".jpeg";
            long size = inputStream.available();
            if (size > Integer.parseInt(maxFileSize)) {
                throw new CuException("文件过大");
            }
            String fix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!images.contains(fix)) {
                throw new CuException("文件类型有误");
            }
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 构建文件路径
            String path = currentWorkingDir + uploadDir + "/" + format;
            Path filePath = Paths.get(path, username + fix);
            Path directoryPath = Paths.get(path);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath); // 创建所有必要的父目录
            }
            // 使用Files.copy将输入流的内容保存到文件中
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            // 更新数据库记录
            String avata = uploadDir + "/" + format + "/" + username + fix;
            userDao.updateAvataByLoginName(username, avata);
        } catch (IOException e) {
            // 处理IO异常
        } finally {
            // 确保输入流关闭，防止资源泄露
            try {
                inputStream.close();
            } catch (IOException e) {
                // 处理流关闭时的异常
            }
        }
    }
}
