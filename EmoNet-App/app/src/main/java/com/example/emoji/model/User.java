package com.example.emoji.model;

/*
*  用户类：存储用户的基本信息
* */

public class User {
    private int uid;
    private String username;
    private String password;
    private String phone;
    private int identity; //用户身份类型：2 = VIP用户，1 = 普通用户

    public User() {}

    public User(String username, String password, String phone, int identity) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.identity = identity;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}
