package com.chances.chancesuser.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具
 *
 * @author lipengcheng
 */
public class PasswordUtils {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 加密密码
    public static String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // 比对密码
    public static boolean matchPassword(String plainPassword, String encodedPassword) {
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }

    // 通过加密密码获取明文密码（仅用于找回密码等情况，不建议在正常业务中使用）
    public static String decodePassword(String encodedPassword) {
        throw new UnsupportedOperationException("不建议解密密码");
    }
}
