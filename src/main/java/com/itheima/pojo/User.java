package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;//主键id
    @Pattern(regexp = "^.{5,16}$")
    private String username;//用户名
    @Pattern(regexp = "^.{5,16}$")
    private String password;//密码
    @NotEmpty
    @Pattern(regexp = "^.{5,16}$")
    private String nickname;//昵称
    @Email
    @NotEmpty
    private String email;//邮箱
    @Pattern(regexp = "^.{5,128}$")
    @URL
    private String userPic;//用户头像地址
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime updateTime;//更新时间
}
