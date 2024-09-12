package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private Integer id;//主键id
    @NotEmpty
    @Pattern(regexp = "^.{2,30}$")
    private String title;//文章标题
    @NotEmpty
    @Pattern(regexp = "^.{2,10000}$")
    private String content;//文章内容
    @NotEmpty
    @URL
    private String coverImg;//文章封面图片
    @NotEmpty
    @Pattern(regexp = "^(已发布|草稿)$")
    private String state;//发布状态 已发布|草稿
    @NotNull
    private Integer categoryId;//分类id
    private Integer createUser;//创建人id
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime updateTime;//更新时间
}
