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
}
