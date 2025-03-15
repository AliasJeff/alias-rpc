package com.alias.example.consumer;

import com.alias.example.common.model.User;
import com.alias.example.common.service.ApiService;
import com.alias.proxy.ServiceProxyFactory;

/**
 * Easy consumer instance
 *
 * @author Jeffery
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // Create a new proxy UserService instance (static)
        // UserService userService = new UserServiceProxy();

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
    }
}
