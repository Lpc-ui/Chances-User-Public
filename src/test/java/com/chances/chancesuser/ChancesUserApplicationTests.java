package com.chances.chancesuser;

import com.chances.chancesuser.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

@SpringBootTest
@AutoConfigureMockMvc
class ChancesUserApplicationTests {

    @Test
    void contextLoads() {
    }
    @Resource
    private UserService userService;

    @Resource
    private MockMvc mockMvc;

    @Test
    public void 登录页_登录_登录成功() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .param("loginName", "adminlpc")
                        .param("password", "qwerty"))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 登录页_登录_用户名密码错误() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .param("loginName", "adminlpc")
                        .param("password", "qwerty1111"))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 登录页_登录_账户锁定() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .param("loginName", "admin")
                        .param("password", "qwerty"))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
