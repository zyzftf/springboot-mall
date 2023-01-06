package com.zhuo.springbootmall.service;

import com.zhuo.springbootmall.dto.UserLoginRequest;
import com.zhuo.springbootmall.dto.UserRegisterRequest;
import com.zhuo.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}
