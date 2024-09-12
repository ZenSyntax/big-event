package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ArticleMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public void addArticle(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.addArticle(article);
    }

    @Override
    public PageBean getArticlesInPage(Integer pageNum, Integer pageSize, Integer userId, Integer categoryId, String state) {
        PageHelper.startPage(pageNum, pageSize);

        List<Article> articles = articleMapper.getArticlesInPage(userId, categoryId, state);
        Page<Article> currentPage = (Page<Article>) articles;

        PageBean pageBean = new PageBean(currentPage.getTotal(), currentPage.getResult());
        return pageBean;
    }

    @Override
    public Article getArticleById(Integer id, Integer userId) {
        Article article = articleMapper.getArticleById(id, userId);
        return article;
    }

    @Override
    public void updateArticle(Integer userId, Article article) throws Exception{
        article.setUpdateTime(LocalDateTime.now());
        int i = articleMapper.updateArticle(userId, article);
        if (i == 0) {
            throw new RuntimeException("没有更新！");
        }
    }

    @Override
    public void deleteArticle(Integer id, Integer userId) throws Exception{
        int i = articleMapper.deleteArticle(id, userId);
        if (i == 0) {
            throw new RuntimeException("删除失败！");
        }
    }
}
