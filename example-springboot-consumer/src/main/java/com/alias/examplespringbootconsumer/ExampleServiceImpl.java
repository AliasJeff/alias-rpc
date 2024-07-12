package com.alias.examplespringbootconsumer;

import com.alias.example.common.model.User;
import com.alias.example.common.service.UserService;
import com.alias.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("alias");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
