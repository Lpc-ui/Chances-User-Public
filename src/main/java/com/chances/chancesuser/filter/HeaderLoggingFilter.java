package com.chances.chancesuser.filter;

import com.chances.chancesuser.cuenum.UserStatusCode;
import com.chances.chancesuser.exception.LockException;
import com.chances.chancesuser.exception.NotLoginException;
import com.chances.chancesuser.model.User;
import com.chances.chancesuser.service.UserService;
import com.chances.chancesuser.utils.JwtUtils;
import com.chances.chancesuser.utils.ServletKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*") // 定义过滤器拦截的URL模式
@Slf4j
public class HeaderLoggingFilter implements Filter {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private UserService userService;


    // 拦截器的初始化早于配置文件::::需要通过构造注入
    public HeaderLoggingFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化操作，可留空
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 获取请求路径
        String requestURI = httpRequest.getRequestURI();
        // 如果请求路径是/user/login，放行请求
        if (requestURI.equals("/user/login")) {
            chain.doFilter(request, response);
        } else {
            // 获取请求头中的header信息
            String token = ((HttpServletRequest) request).getHeader("token");
            if (StringUtils.isEmpty(token)) throw new NotLoginException();
            // 在此处处理headerValue，可以打印、记录、验证等操作
            String userName;
            try {
                userName = jwtUtils.getUsernameFromToken(token);
                if (!jwtUtils.isTokenValid(token, userName)) throw new NotLoginException();
                log.info(userName + ":" + "登录");
                log.info(token + ":" + "token");
                User user = userService.findByName(userName);
                Integer status = user.getStatus();
                if (status.equals(UserStatusCode.LOCK.code())) throw new LockException();
                if (status.equals(UserStatusCode.DISABLE.code())) throw new LockException();
            } catch (Exception e) {
                e.printStackTrace();
                ServletKit.getRequest().setAttribute("exception", e);
                ServletKit.getRequest().getRequestDispatcher("/error").forward(ServletKit.getRequest(), ServletKit.getResponse());
                return;
            }

            // 继续处理请求
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // 销毁操作，可留空
    }
}
