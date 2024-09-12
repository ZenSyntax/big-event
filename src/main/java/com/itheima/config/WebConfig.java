package com.itheima.config;

import com.itheima.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//声明配置类
//@EnableWebMvc//启用mvc配置类，一旦启用，拦截器将失效，程序无法正常返回
public class WebConfig implements WebMvcConfigurer {
//    不做配置，则关闭springboot对静态资源的请求处理操作
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //在这个方法中配置拦截器的拦截范围，/**表示拦截/下的所有层级的请求，/*表示只拦截/下的第一层级的请求，
        //excludePathPatterns表示排除某个路径
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/user/login", "/user/register");
    }
}
