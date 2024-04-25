package com.alias.example.common.model;

import java.io.Serializable;

/**
 * User model
 *
 * @author Jeffery
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
