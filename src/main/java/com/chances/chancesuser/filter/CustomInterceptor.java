package com.chances.chancesuser.filter;

import com.chances.chancesuser.cuenum.UserStatusCode;
import com.chances.chancesuser.exception.LockException;
import com.chances.chancesuser.exception.NotLoginException;
import com.chances.chancesuser.model.User;
import com.chances.chancesuser.service.UserService;
import com.chances.chancesuser.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        // 如果请求路径是/user/login，放行请求
        if ("/user/login".equals(requestURI)) {
            return true;
        }
        // 其他请求处理
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new NotLoginException();
        }
        // Token验证和用户状态检查
        String userName;
        try {
            userName = jwtUtils.getUsernameFromToken(token);
        } catch (Exception e) {
            throw new NotLoginException();
        }
        if (!jwtUtils.isTokenValid(token, userName)) {
            throw new NotLoginException();
        }
        log.info(userName + ":" + "登录");
        log.info(token + ":" + "token");
        User user = userService.findByName(userName);
        Integer status = user.getStatus();
        if (status.equals(UserStatusCode.LOCK.code())) {
            throw new LockException();
        }
        if (status.equals(UserStatusCode.DISABLE.code())) {
            throw new LockException();
        }

        // 验证成功，继续处理请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可以添加处理完成后的逻辑，比如日志记录等
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求结束之后调用，主要用于进行资源清理
    }
}
