package com.zhuo.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuo.springbootmall.dao.UserDao;
import com.zhuo.springbootmall.dto.UserLoginRequest;
import com.zhuo.springbootmall.dto.UserRegisterRequest;
import com.zhuo.springbootmall.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Transactional  // 將資料庫的改動 rollback 回去
    @Test
    public void register_success() throws Exception{    //  註冊成功的情況

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test1@gmail.com");
        userRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.userId", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo("test1@gmail.com")))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

        // 檢查資料庫的密碼不為明碼
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        assertNotNull(user.getPassword(), userRegisterRequest.getPassword());
    }

    @Test
    public void register_invalidEmailFormat() throws Exception{  //  前端傳送email的格式不正確

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("qieewiwoqjjss");
        userRegisterRequest.setPassword("456");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void register_emailAlreadyExist() throws Exception{    // 假設這筆email帳號已經存在, 就無法創建該筆帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test2@gmail.com");
        userRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));   //  call這個 api, 創建新帳號

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));  //   帳號已經存在, 無法創建該筆帳號
    }

    @Test
    public void login_success() throws Exception {  //  登入成功

        //  先創建一筆新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test3@gmail.com");
        userRegisterRequest.setPassword("123");

        register(userRegisterRequest);

        //  登入帳號
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("test3@gmail.com");
        userLoginRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

    }

    @Test
    public void login_wrongPassword() throws Exception{  // 登入密碼錯誤
        //  先創建一筆新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test4@gmail.com");
        userRegisterRequest.setPassword("123");

        register(userRegisterRequest);

        //  登入帳號
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(userRegisterRequest.getEmail());
        userLoginRequest.setPassword("456");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));     //  密碼錯誤, 登入失敗!
    }

    @Test
    public void login_invalidEmailFormat() throws Exception{   //  email格式錯誤
        //  登入帳號
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("dhdjfshshh");
        userLoginRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void login_emailNotExist() throws Exception{  //  登入一個不存在的帳號
        //  登入帳號
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("test5@gmail.com");
        userLoginRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    private void register(UserRegisterRequest userRegisterRequest) throws Exception{
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));
    }

}