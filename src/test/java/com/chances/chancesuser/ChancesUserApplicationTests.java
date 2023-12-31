package com.chances.chancesuser;

import com.chances.chancesuser.cuenum.UserStatusCode;
import com.chances.chancesuser.dao.UserDao;
import com.chances.chancesuser.exception.LockException;
import com.chances.chancesuser.exception.PwdNotMatchException;
import com.chances.chancesuser.model.User;
import com.chances.chancesuser.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("all")
class ChancesUserApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    //登录页_登录_登录成功 测试生成
    public static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbmxwYyIsImlhdCI6MTY5ODIyNzg4NiwiZXhwIjoxNjk4MjMxNDg2fQ.LGh6vJe9wB8xA5N1Yen82qjzxCldq4gvsiiBMPTLoD5wIiyEfm1T5BIaTRU1eXT_a4M4Es8HaGoku_pW5ybbfQ";

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data", hasKey("token"))) //存在token
                .andExpect(jsonPath("$.msg", is("登录成功")))
                .andExpect(jsonPath("$.code", is(200)));
    }

    @Test
    public void 登录页_登录_用户名密码错误() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .param("loginName", "adminlpc")
                        .param("password", "qwerty1111"))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code", is(PwdNotMatchException.CODE))) // 使用jsonPath断言code的值
                .andExpect(jsonPath("$.msg", is("用户名或密码错误"))) // 使用jsonPath断言msg的值
                .andExpect(jsonPath("$.data", is(nullValue()))); // 使用jsonPath断言data的值;
    }

    @Test
    public void 登录页_登录_账户锁定() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .param("loginName", "admin")
                        .param("password", "qwerty"))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data", is(nullValue())))
                .andExpect(jsonPath("$.msg", is("用户被锁定")))
                .andExpect(jsonPath("$.code", is(LockException.CODE)));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.jsonList", iterableWithSize(10)))  //包含10条数据
                .andExpect(jsonPath("$.msg", is("条件分页")))
                .andExpect(jsonPath("$.code", is(200)));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.msg", is("条件分页")))
                //每个手机号都包含4
                .andExpect(jsonPath("$.data.jsonList[*].mobile", everyItem(containsString("4"))))
                //每个邮箱都包含testuser
                .andExpect(jsonPath("$.data.jsonList[*].email", everyItem(containsString("testuser"))));
    }

    @Test
    public void 用户管理_用户列表_下一页() throws Exception {
        String path = "/user/list";
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN)
                        .param("pageNum", "2")
                )
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.msg", is("条件分页")))
                //第二页
                .andExpect(jsonPath("$.data.pageNum", is(2)));
    }

    @Test
    public void 用户管理_用户列表_设置单页数据量() throws Exception {
        String path = "/user/list";
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN)
                        .param("pageSize", "20")
                )
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk())
                //每页20
                .andExpect(jsonPath("$.data.pageSize", is(20)));
    }

    @Test
    public void 用户管理_用户列表_查看() throws Exception {
        //查看用户ID为24的用户
        String path = "/user/24";
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"example\"}")
                        .header("token", TOKEN)
                )
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.msg", is("用户信息")))
                .andExpect(jsonPath("$.data.loginName", is("adminlpc")));
    }

    @Test
    public void 用户管理_用户列_编辑() throws Exception {
        String userId = "24";
        String requestBody = "{\"mobile\": \"1888888888\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String mobile = userDao.findById(Long.valueOf(userId)).orElse(null).getMobile();
        // 断言mobile的值是否等于"1888888888"
        assertEquals("1888888888", mobile);
    }

    @Test
    public void 用户管理_新增用户() throws Exception {
        String requestBody = "{\"loginName\": \"lpcNew\",\"mobile\": \"1353000333\",\"email\": \"fsafsfa@gmial.com\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        User user = userService.findByName("lpcNew");
        // 断言不为null
        assertNotNull(user);
    }

    @Test
    public void 用户管理_用户列表_删除() throws Exception {
        User lpcNew = userService.findByName("lpcNew");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + lpcNew.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        User user = userService.findByName("lpcNew");
        // 断言为null--已删除
        assertNull(user);
    }

    @Test
    public void 用户管理_用户列表_锁定解锁() throws Exception {
        //锁定否不能访问接口
        User adminlpc = userService.findByName("adminlpc");
        Long userId = adminlpc.getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/update/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                        .param("status", String.valueOf(UserStatusCode.DISABLE.code()))
                        .param("userId", String.valueOf(userId))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        User user = userService.findByName("adminlpc");
        // 断言禁用
        assertEquals(UserStatusCode.DISABLE.code(), user.getStatus());
        //解锁
        userService.lock(userId, String.valueOf(UserStatusCode.OK.code()));
    }


    @Test
    public void 头像_用户信息() throws Exception {
        // 0 表示获取当前登录用户的信息
        String userId = "0";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.msg", is("用户信息")))
                .andExpect(jsonPath("$.data.loginName", is("adminlpc")))
                .andReturn();
    }

    @Test
    public void 头像_用户信息_信息修改() throws Exception {
        // 0 表示获取当前登录用户的信息
        String userId = "0";
        String requestBody = "{\"mobile\": \"199999999\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        User user = userService.findByName("adminlpc");
        // 断言禁用
        assertEquals("199999999", user.getMobile());
    }

    @Test
    public void 头像_修改密码_修改失败() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/update/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                        .param("oldPassword", "errorpassword")
                        //新的密码
                        .param("newPassword", "qwertyu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void 头像_修改密码_修改成功() throws Exception {
        //注意:::修改密码后需要重新登录生成新的TOKEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/update/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN)
                        .param("oldPassword", "qwerty")
                        //新的密码
                        .param("newPassword", "qwertyu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    @Test
    public void 头像_上传头像_上传成功() throws Exception {
        File file1 = new File("/Users/lipengcheng/Chances-User/src/images/adminlpc-test.png");
        try (FileInputStream fileInputStream = new FileInputStream(file1)) {

            // 创建一个模拟的MultipartFile
            MockMultipartFile file = new MockMultipartFile(
                    "file", // 请求参数名，应与Controller中@RequestParam("file")的名称匹配
                    "adminlpc-test.png", // 文件名
                    MediaType.IMAGE_PNG.toString(), // 文件类型
                    fileInputStream // 文件内容
            );

            // 发送POST请求，模拟文件上传
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/user/avatar/upload").file(file)
                            .header("token", TOKEN))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }
    }

    @Test
    public void 头像_上传头像_文件过大() throws Exception {
        File file1 = new File("/Users/lipengcheng/Chances-User/src/images/big-file-test.png");
        try (FileInputStream fileInputStream = new FileInputStream(file1)) {

            // 创建一个模拟的MultipartFile
            MockMultipartFile file = new MockMultipartFile(
                    "file", // 请求参数名，应与Controller中@RequestParam("file")的名称匹配
                    "big-file-test.png", // 文件名
                    MediaType.IMAGE_PNG.toString(), // 文件类型
                    fileInputStream // 文件内容
            );

            // 发送POST请求，模拟文件上传
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/user/avatar/upload").file(file)
                            .header("token", TOKEN))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }
    }

    @Test
    public void 头像_上传头像_文件类型错误() throws Exception {
        File file1 = new File("/Users/lipengcheng/Chances-User/src/images/adminlpc.pdf");
        try (FileInputStream fileInputStream = new FileInputStream(file1)) {
            // 创建一个模拟的MultipartFile
            MockMultipartFile file = new MockMultipartFile(
                    "file", // 请求参数名，应与Controller中@RequestParam("file")的名称匹配
                    "adminlpc.pdf", // 文件名
                    MediaType.APPLICATION_PDF.toString(), // 文件类型
                    fileInputStream // 文件内容
            );

            // 发送POST请求，模拟文件上传
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/user/avatar/upload").file(file)
                            .header("token", TOKEN))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }
    }

    @Test
    public void 后台页_登出() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", TOKEN))
                .andDo(MockMvcResultHandlers.print()) // 打印响应内容
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg", is("退出成功")))
                .andExpect(jsonPath("$.code", is(200)));
    }
}