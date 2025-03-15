package com.alias.example.consumer;

import com.alias.bootstrap.ConsumerBootstrap;
import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;
import com.alias.proxy.ServiceProxyFactory;

/**
 * @author Jeffery
 */
public class ConsumerExample {

    public static void main(String[] args) {

        ConsumerBootstrap.init();

        // Create a new proxy UserService instance (dynamic)
        ApiService apiService = ServiceProxyFactory.getProxy(ApiService.class);

        User user = new User();
        user.setName("Jeffery");
        // Call the getUser method
        User newUser = apiService.getUser(user);
        if (newUser != null) {
            System.out.println("New user：" + newUser.getName());
        } else {
            System.out.println("Failed to get user, user == null");
        }
        long number = apiService.getNumber();
        System.out.println("number: " + number);
    }
}
