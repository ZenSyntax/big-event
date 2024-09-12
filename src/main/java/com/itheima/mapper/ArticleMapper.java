package com.itheima.mapper;

import com.itheima.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time)" +
            "values (#{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, #{createUser}, #{createTime}, #{updateTime})")
    void addArticle(Article article);

    List<Article> getArticlesInPage(Integer userId, Integer categoryId, String state);

    @Select("select * from article where id = #{id} and create_user = #{userId}")
    Article getArticleById(Integer id, Integer userId);

    @Update("update article set title = #{article.title}, content = #{article.content}, cover_img = #{article.coverImg}, state = #{article.state}, category_id = #{article.categoryId} " +
            "where id = #{article.id} and create_user = #{userId}")
    int updateArticle(Integer userId, Article article);

    @Delete("delete from article where id = #{id} and create_user = #{userId}")
    int deleteArticle(Integer id, Integer userId);
}
