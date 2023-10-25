package com.chances.chancesuser.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletKit {
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * 上传图片的时候需要：服务器路径+上下文访问路径（所以封装了该方法）
     *
     * @return 服务地址
     */
    public static String getUrl() {
        HttpServletRequest request = getRequest();
        return getUrl(request);
    }

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * 上传图片的时候需要：服务器路径+上下文访问路径（所以封装了该方法）
     *
     * @return 服务地址
     */
    public static String getUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
