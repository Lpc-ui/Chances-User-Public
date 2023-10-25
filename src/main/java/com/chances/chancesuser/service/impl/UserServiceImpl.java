package com.chances.chancesuser.service.impl;

import com.chances.chancesuser.base.ErrorCode;
import com.chances.chancesuser.base.PageJson;
import com.chances.chancesuser.base.Result;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

    @Override
    public void test() {
        User entity = new User();
        entity.setLoginName("李鹏成");
        entity.setPassword("12345");
        entity.setMobile("18286703704");
        entity.setEmail("lpcxyxy@gmail.com");
        entity.setAvata("qqq");
        entity.setAdmin(0);
        entity.setStatus(0);
        entity.setLastLoginTime(LocalDateTime.now());
        userDao.insert(entity);
    }

    @Override
    public void add(UserDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getLoginName())) throw new CuException("用户名不能为空");
        if (StringUtils.isEmpty(userDTO.getMobile())) throw new CuException("手机号不能为空");
        if (StringUtils.isEmpty(userDTO.getEmail())) throw new CuException("邮箱不能为空");
        if (ObjectUtils.isEmpty(userDTO.getAdmin())) userDTO.setAdmin(UserAdminCode.NO_ADMIN.getCode());
        if (ObjectUtils.isEmpty(userDTO.getStatus())) userDTO.setStatus(UserStatusCode.OK.code());
        //初始密码
        userDTO.setLastLoginTime(LocalDateTime.now());
        userDTO.setPassword(PasswordUtils.encodePassword(defaultPassword));
        User user = BeanCopyUtil.copyBeanProperties(userDTO, User::new);
        userDao.insert(user);
    }

    @Override
    public String userLogin(String loginName, String password) {
        User user = userDao.findByLoginName(loginName);
        if (ObjectUtils.isEmpty(user)) throw new PwdNotMatchException();
        if (!PasswordUtils.matchPassword(password, user.getPassword())) throw new PwdNotMatchException();
        if (user.getStatus().equals(UserStatusCode.LOCK.code()))
            throw new LockException();
        if (user.getStatus().equals(UserStatusCode.DISABLE.code()))
            throw new LockException();
        String token = jwtUtils.generateToken(loginName);
        userDao.updateLastLoginTimeById(user.getId(), LocalDateTime.now());
        return token;
    }

    @Override
    public void logout(String token) {
        String userName = jwtUtils.getUsernameFromToken(token);
        if (StringUtils.isEmpty(userName)) return;
        jwtUtils.invalidateToken(userName);
    }

    @Override
    public User findByName(String userName) {
        return userDao.findByLoginName(userName);
    }

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
                .peek(userDTO -> userDTO.setLastLoginTime(null))
                .collect(Collectors.toList());
        data.setJsonList(userDTOSNew);
        return data;
    }

    @Override
    public void userDelete(String userId) {
        userDao.deleteById(Long.valueOf(userId));
    }

    @Override
    public UserDTO userInfo(String userId, String token) {
        User user = this.getUser(userId, token);
        UserDTO userDTO = BeanCopyUtil.copyBeanProperties(user, UserDTO::new);
        userDTO.setAvata(null);
        userDTO.setStatus(null);
        userDTO.setPassword(null);
        userDTO.setLastLoginTime(null);
        return userDTO;
    }

    @Override
    public void userUpdate(String userId, UserDTO userDTO, String token) {
        User user = this.getUser(userId, token);
        if (user == null) throw new CuException("参数有误");
        if (ObjectUtils.isNotEmpty(userDTO.getStatus())) user.setStatus(userDTO.getStatus());
        if (ObjectUtils.isNotEmpty(userDTO.getAdmin())) user.setAdmin(userDTO.getAdmin());
        if (ObjectUtils.isNotEmpty(userDTO.getEmail())) user.setEmail(userDTO.getEmail());
        if (ObjectUtils.isNotEmpty(userDTO.getMobile())) user.setMobile(userDTO.getMobile());
        //手机号校验
        if (ObjectUtils.isNotEmpty(userDTO.getMobile()) && userDao.existsByMobile(userDTO.getMobile()))
            throw new CuException("手机号重复");
        userDao.save(user);
    }

    private User getUser(String userId, String token) {
        return userId.equals("0")
                ? userDao.findByLoginName(jwtUtils.getUsernameFromToken(token))
                : userDao.findById(Long.valueOf(userId)).orElse(null);
    }

    @Override
    public void lock(String userId, String status) {
        userDao.updateStatusById(Long.parseLong(userId), Integer.parseInt(status));
    }

    @Override
    public void password(String oldPassword, String newPassword, String token) throws Exception {
        String username = jwtUtils.getUsernameFromToken(token);
        User user = userDao.findByLoginName(username);
        if (!PasswordUtils.matchPassword(oldPassword, user.getPassword())) throw new CuException("原密码有误");
        if (StringUtils.isEmpty(newPassword)) throw new CuException("请输入新密码");
        userDao.updatePasswordById(user.getId(), PasswordUtils.encodePassword(newPassword));
        //移除token[退出登录]
        jwtUtils.invalidateToken(username);
    }


    @Value("${chances.upload.dir}")
    private String uploadDir;


    private final List<String> images = Arrays.asList(".png", ".jpg", ",jpg");

    @Override
    public Result setImage(MultipartFile file, String token) {
        try {
            // 构建文件路径
            String username = jwtUtils.getUsernameFromToken(token);
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            if (size > 2000000) throw new CuException("文件过大");
            assert originalFilename != null;
            String fix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!images.contains(fix)) throw new CuException("文件类型有误");
            Path filePath = Paths.get(uploadDir, username + fix);
            // 将文件保存到指定路径
            file.transferTo(filePath.toFile());
            userDao.updateAvataByLoginName(username, filePath.toFile().getPath());
            return Result.ok().setMsg("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(ErrorCode.CU_EX).setMsg("上传失败");
        }
    }
}
