package com.itheima.controller;


import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtils;
import com.itheima.utils.RegexUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register (@Pattern(regexp = "^.{5,16}$") @NotNull @NotEmpty String username,
                            @Pattern(regexp = "^.{5,16}$") @NotNull @NotEmpty String password) {
        //查询用户
        User user = userService.findByUsername(username);
        if (user == null) {
            //新用户
            userService.register(username, password);
            return Result.success();
        } else {
            //不存在用户
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^.{5,16}$") @NotNull @NotEmpty String username,
                        @Pattern(regexp = "^.{5,16}$") @NotNull @NotEmpty String password) {
        //查询用户
        User user = userService.findByPass(username, password);
        if (user != null) {
            //分发jwt
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", username);
            String jwt = JwtUtils.generateJwt(claims);
            /*将登录时生成的token传入redis，键值一致为token*/
            stringRedisTemplate.opsForValue().set(jwt, jwt, 1, TimeUnit.HOURS);
            return Result.success(jwt);
        } else {
            //用户不存在
            return Result.error("用户不存在");
        }
    }

    @GetMapping("/userInfo")
    public Result<User> getUserInfo() {
        //根据用户名查询用户
        Map<String, Object> claims = ThreadLocalUtil.get();//从ThreadLocal中获取
        String username = claims.get("username").toString();
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody @Validated User user) {
        RegexUtil.isValidVal("^[0-9]{1,6}$", user.getId());//检查传入的id是否符合规范
        Map<String, Object> claims = ThreadLocalUtil.get();//从ThreadLocal中获取
        String username = claims.get("username").toString();
        Integer id = (Integer)claims.get("id");
        log.info("请求头的数据：name：{}， id：{}，请求参数的数据：name：{}， id：{}", username, id, user.getUsername(), user.getId());
        if (user.getUsername() != null) {
            if (!username.equals(user.getUsername()) || id != user.getId()) {
                return Result.error("你不能操作别人的数据");
            }
        } else {
            if (id != user.getId()) {
                return Result.error("你不能操作别人的数据");
            }
        }
        userService.updateUserInfo(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")//只修改单个参数就使用patch
    public Result updateAvatar(@Pattern(regexp = "^.{5,128}$") @NotNull @NotEmpty String avatarUrl) {
        Map<String, Object> claims = ThreadLocalUtil.get();//从ThreadLocal中获取
        String username = claims.get("username").toString();
        User user = new User();
        user.setUserPic(avatarUrl);
        user.setUsername(username);
        userService.updateUserInfo(user);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody @NotNull @NotEmpty Map<String, String> params,
                            @RequestHeader("Authorization") String token) throws Exception{
        if (params.size() != 3) {
            return Result.error("传入的参数有误");
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            RegexUtil.isValidVal("^.{5,16}$", key);
            RegexUtil.isValidVal("^.{5,16}$", value);
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = claims.get("username").toString();
        userService.updatePwd(username, params.get("old_pwd"), params.get("new_pwd"), params.get("re_pwd"));
        /*当修改密码成功后，从redis中直接删除旧token*/
        stringRedisTemplate.opsForValue().getOperations().delete(token);
        return Result.success();
    }
}
