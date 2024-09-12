package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username} and password = #{password}")
    User findByPass(String username, String password);

    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    @Insert("insert into user(username, password, create_time, update_time)" +
            " values(#{username}, #{md5String}, now(), now())")
    void addUser(String username, String md5String);

    void updateUser(User user);

    @Update("update user set password = #{newPwd} where username = #{username}")
    void updatePwd(String username, String newPwd);
}
