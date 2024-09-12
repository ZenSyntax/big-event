package com.itheima.controller;

import com.itheima.pojo.Category;
import com.itheima.pojo.Result;
import com.itheima.service.CategoryService;
import com.itheima.utils.RegexUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result addCategory(@RequestBody @Validated Category category) {
        /*controller主要处理请求参数的获取和规范性检查，不处理业务逻辑相关的判断和数据的获取*/


        /*请求体参数的获取和规范性判断*/
        String categoryName = category.getCategoryName();
        String categoryAlias = category.getCategoryAlias();
        log.info("分类名：" + categoryName + "|" + "别名：" + categoryAlias);

        /*请求头参数的获取和规范性判断*/
        //获得创建人
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer)claims.get("id");

        //封装category
        category.setCreateUser(id);
        categoryService.addCategory(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> getCategory() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer)claims.get("id");
        List<Category> categoryList = categoryService.selectCategoryByCreateUser(id);
        return Result.success(categoryList);
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id) {
        RegexUtil.isValidVal("^[0-9]{1,6}$", id);
        Category category = categoryService.selectCategoryById(id);
        return Result.success(category);
    }

    @PutMapping
    public Result updateCategory(@RequestBody @Validated Category category) {
        categoryService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteCategory(Integer id) {
        RegexUtil.isValidVal("^[0-9]{1,6}$", id);
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
