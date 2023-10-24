package com.chances.chancesuser.config;

import com.chances.chancesuser.filter.HeaderLoggingFilter;
import com.chances.chancesuser.service.UserService;
import com.chances.chancesuser.utils.JwtUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

@Configuration
public class FilterConfig {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private UserService userService;

    // 注册过滤器的方式一：通过@Bean注解注册
    @Bean
    @Order(1) // 指定过滤器的执行顺序
    public FilterRegistrationBean<HeaderLoggingFilter> headerLoggingFilter() {
        FilterRegistrationBean<HeaderLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HeaderLoggingFilter(jwtUtils, userService));
        registrationBean.addUrlPatterns("/*"); // 定义过滤器拦截的URL模式
        return registrationBean;
    }
}
