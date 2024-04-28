package com.alias.example.provider;

import com.alias.example.common.model.User;
import com.alias.example.common.service.UserService;

/**
 * 用户服务实现类
 *
 * @author Jeffery
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("User name: " + user.getName());
        return user;
    }
}
