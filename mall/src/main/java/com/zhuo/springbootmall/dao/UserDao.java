package com.zhuo.springbootmall.dao;

import com.zhuo.springbootmall.dto.UserRegisterRequest;
import com.zhuo.springbootmall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
