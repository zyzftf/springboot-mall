package com.zhuo.springbootmall.service.impl;

import com.zhuo.springbootmall.dao.UserDao;
import com.zhuo.springbootmall.dto.UserRegisterRequest;
import com.zhuo.springbootmall.model.User;
import com.zhuo.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
