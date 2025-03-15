package com.alias.examplespringbootconsumer;

import com.alias.example.common.model.Order;
import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;
import com.alias.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExampleController {

    @RpcReference
    private ApiService apiService;

    @GetMapping("/user")
    public User getUserInfoById(@RequestParam Long userId) {
        return apiService.getUserById(userId);
    }

    @PostMapping("/orders")
    public List<Order> fetchOrdersByUserId(@RequestBody Order order) {
        return apiService.getOrdersByUserId(order.getUserId());
    }

}
