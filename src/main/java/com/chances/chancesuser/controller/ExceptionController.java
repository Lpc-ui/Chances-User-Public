package com.chances.chancesuser.controller;

import com.chances.chancesuser.base.Result;
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
    public Result post(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @GetMapping("/error")
    public Result get(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @PutMapping("/error")
    public Result put(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }

    @DeleteMapping("/error")
    public Result deleted(HttpServletRequest request) throws Exception {
        throw (Exception) request.getAttribute("exception");
    }
}
