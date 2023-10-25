package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 通过ExceptionUtils抛出的异常
 * 回进入Controller再次进行抛出:目的是为了GlobalExceptionConfig全局异常能获取到
 * 用于能处理过滤器中的异常
 */
@RestController
public class ExceptionController {
    @PostMapping("/error")
    public R post(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @GetMapping("/error")
    public R get(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @PutMapping("/error")
    public R put(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @DeleteMapping("/error")
    public R deleted(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }
}
