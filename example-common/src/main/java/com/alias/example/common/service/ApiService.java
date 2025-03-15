package com.alias.example.common.service;

import com.alias.example.common.model.Order;
import com.alias.example.common.model.User;

import java.util.List;

/**
 * User service
 *
 * @author Jeffery
 */
public interface ApiService {

    /**
     * Get user
     *
     * @param user
     * @return
     */
    User getUser(User user);

    User getUserById(Long userId);

    default short getNumber() {
        return 1;
    }

    List<Order> getOrdersByUserId(Long userId);

}
