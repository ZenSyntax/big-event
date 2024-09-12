package com.itheima.controller;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.ArticleService;
import com.itheima.utils.RegexUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result addArticle(@Validated @RequestBody Article article) {
        RegexUtil.isValidVal("^[0-9]{1,6}$", article.getCategoryId());
        //通过规范性检查后，将创建人id加入该对象
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        article.setCreateUser(id);
        //将通过规范性检查并打包好的article对象传入service层
        articleService.addArticle(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> getArticlesInPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                       @RequestParam(defaultValue = "5") Integer pageSize,
                                                       Integer categoryId,
                                                       @Pattern(regexp = "^(已发布|草稿)$") String state) {
        RegexUtil.isValidVal("^[0-9]{1,2}$", pageNum);
        RegexUtil.isValidVal("^[0-9]{1,2}$", pageSize);
        RegexUtil.isValidVal("^[0-9]{1,6}$", categoryId);
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        PageBean pageBean = articleService.getArticlesInPage(pageNum, pageSize, userId, categoryId, state);
        return Result.success(pageBean);
    }

    @GetMapping("/detail")
    public Result detail(@NotNull Integer id) {
        RegexUtil.isValidVal("^[0-9]{1,6}$", id);
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        Article article = articleService.getArticleById(id, userId);
        if (article != null) {
            return Result.success(article);
        }
        return Result.error("不存在该文章");
    }

    @PutMapping
    public Result updateArticle(@Validated @RequestBody Article article) throws Exception{
        RegexUtil.isValidVal("^[0-9]{1,6}$", article.getId());
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        articleService.updateArticle(userId, article);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteArticle(@RequestParam Integer id) throws Exception{
        RegexUtil.isValidVal("^[0-9]{1,6}$", id);
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        articleService.deleteArticle(id, userId);
        return Result.success();
    }

}
