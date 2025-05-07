package com.alias.examplespringbootconsumer;

import com.alias.example.common.model.Order;
import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;
import com.alias.rpc.springboot.starter.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ExampleController {

    @RpcReference
    private ApiService apiService;

    @GetMapping("/user")
    public User getUserInfoById(@RequestParam Long userId) throws InterruptedException {
        log.info("[getUserInfoById] Starting to fetch user info for userId: {}", userId);
        User user = apiService.getUserById(userId);

        log.info("[getUserInfoById] Executed successfully, parameter: {}, result: {}", userId, user);
        return user;
    }

    @PostMapping("/orders")
    public List<Order> fetchOrdersByUserId(@RequestBody Order order) {
        log.info("[fetchOrdersByUserId] Starting to fetch orders for userId: {}", order.getUserId());
        List<Order> orders = apiService.getOrdersByUserId(order.getUserId());
        log.info("[fetchOrdersByUserId] Executed successfully, parameter: {}, result: {}", order.getUserId(), orders);
        return orders;
    }

    @GetMapping("/user/error")
    public String fetchUserError(@RequestParam Long userId) throws InterruptedException {
        log.info("[fetchUserError] Starting to fetch user info for userId: {}", userId);
//        Thread.sleep(100);
        log.info("[fetchUserError] Executed successfully, parameter: {}", userId);
        return "[CustomException] Test exception, parameter: " + userId;
    }

}
