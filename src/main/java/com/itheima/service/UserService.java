package com.itheima.service;

import com.itheima.pojo.User;

public interface UserService {

    User findByPass(String username, String password);

    void register(String username, String password);

    void updateUserInfo(User user);

    void updatePwd(String username, String ordPwd, String newPwd, String rePwd) throws Exception;

    User findByUsername(String username);
}
