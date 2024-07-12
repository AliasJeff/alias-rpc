package com.alias.examplespringbootprovider;

import com.alias.example.common.model.User;
import com.alias.example.common.service.UserService;
import com.alias.rpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("UserServiceImpl.getUser => " + user.getName());
        return user;
    }
}
