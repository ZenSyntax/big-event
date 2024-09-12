package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByPass(String username, String password) {
        String md5String = Md5Util.getMD5String(password);
        User u = userMapper.findByPass(username, md5String);
        return u;
    }

    @Override
    public void register(String username, String password) {
        //密码加密处理，使用MD5加密算法
        String md5String = Md5Util.getMD5String(password);
        //添加
        userMapper.addUser(username, md5String);
    }

    @Override
    public void updateUserInfo(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        return user;
    }

    @Override
    public void updatePwd(String username, String ordPwd, String newPwd, String rePwd) throws Exception {
        //md5加密字符串转换
        String md5OldPwd = Md5Util.getMD5String(ordPwd);
        //检查旧密码是否正确
        User user = userMapper.findByPass(username, md5OldPwd);
        if (user == null){
            throw new RuntimeException("原密码输入错误");
        }
        //然后检查密码前后输入是否一致
        if (!newPwd.equals(rePwd)){
            throw new RuntimeException("密码前后不一致");
        }
        String md5NewPwd = Md5Util.getMD5String(newPwd);
        userMapper.updatePwd(username, md5NewPwd);
    }
}
