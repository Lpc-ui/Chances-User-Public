package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.ErrorCode;
import com.chances.chancesuser.base.R;
import com.chances.chancesuser.service.UserService;
import com.chances.chancesuser.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    @Value("${chances.upload.dir}")
    private String uploadDir;

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @PostMapping("/upload")
    @ResponseBody
    public R uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader String token) {
        try {
            // 构建文件路径
            String username = jwtUtils.getUsernameFromToken(token);

            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String fix = originalFilename.substring(originalFilename.lastIndexOf("."));
            Path filePath = Paths.get(uploadDir, username + fix);
            // 将文件保存到指定路径
            file.transferTo(filePath.toFile());
            userService.setImage(username, filePath.toFile().getPath());
            return R.ok().setMsg("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return R.failed(ErrorCode.CU_EX).setMsg("上传失败");
        }
    }
}
