package com.alias.example.provider;

import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;

/**
 * 用户服务实现类
 *
 * @author Jeffery
 */
public class ApiServiceImpl implements ApiService {

    @Override
    public User getUser(User user) {
        System.out.println("User name: " + user.getName());
        return user;
    }
}
