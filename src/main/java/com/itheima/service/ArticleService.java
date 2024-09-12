package com.itheima.service;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;

public interface ArticleService {
    void addArticle(Article article);

    PageBean getArticlesInPage(Integer pageNum, Integer pageSize, Integer userId, Integer categoryId, String state);

    Article getArticleById(Integer id, Integer userId);

    void updateArticle(Integer userId, Article article) throws Exception;

    void deleteArticle(Integer id, Integer userId) throws Exception;
}
