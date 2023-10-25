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

    //登录页_登录_登录成功 测试生成
    public static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbmxwYyIsImlhdCI6MTY5ODIxMjM0NiwiZXhwIjoxNjk4MjE1OTQ2fQ.gC3egYQcteTzZOA0PcYUyJ67eyhNhjXY-Eo28KDTPd3csul7vzWPiU9Ku5a1MbztHRoluy6jy6n1lMm0CrpYAg";

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


    @Test
    public void 后台页_登出() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // TODO: 未登录调用返回不了结果  直接调用可以返回
    @Test
    public void 用户管理_用户列表() throws Exception {
        String path = "/user/list";
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 用户管理_用户列表_条件搜索() throws Exception {
        //手机号包含4,邮箱包含testuser的用户
        String path = "/user/list";
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN)
                        .param("email", "testuser")
                        .param("mobile", "4"))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void 用户管理_用户列表_下一页() throws Exception {
        String path = "/user/list";
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN)
                        .param("pageSize", "2")
                )
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
