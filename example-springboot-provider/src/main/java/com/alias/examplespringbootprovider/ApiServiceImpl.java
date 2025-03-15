package com.alias.examplespringbootprovider;

import com.alias.example.common.model.Order;
import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;
import com.alias.rpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RpcService
public class ApiServiceImpl implements ApiService {
    @Override
    public User getUser(User user) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return new User(userId, "alias", "zhexunchen@gmail.com");
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return List.of(
                new Order(userId, 101L, "Laptop", 1),
                new Order(userId, 102L, "Keyboard", 2)
        );
    }
}
