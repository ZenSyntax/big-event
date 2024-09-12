package com.itheima.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import com.itheima.utils.ThreadLocalUtil;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 拦截器只能拦截进入spring环境内的资源访问请求
 * 并且需要定义一个配置类来配置拦截的内容，如本项目的WebConfig.java
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override// 目标资源方法运行前运行，返回true表示放行，返回false不放行
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        //1.获取请求url
        String url = req.getRequestURL().toString();
        log.info("请求的url: {}", url);

        //2.判断请求url中是否包含login，如果包含，说明是登录操作，放行
        if (url.contains("/user/login") || url.contains("/user/register")) {
            log.info("登录或注册操作，放行...");
            return true;
        }

        //3.获取请求头中的令牌（token）
        String jwt = req.getHeader("Authorization");

        //4.判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if (!StringUtils.hasLength(jwt)) {//hasLength用于检查字符串是否有长度，即要求字符串既不为null又不为''时为true，否则为false
            log.info("请求头token为空，返回未登录信息");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);//手动将对象转换为json格式，借助阿里巴巴fastJSON工具依赖
            resp.getWriter().write(notLogin);
            return false;
        }

        //5.解析token，如果解析失败，返回错误结果（未登录）
        try {
            Jws jws = JwtUtils.parseJwt(jwt);
            /*借助redis判断新旧令牌是否一致*/
            String redisToken = stringRedisTemplate.opsForValue().get(jwt);
            if (!StringUtils.hasLength(redisToken)) {
                //token失效了
                throw new RuntimeException("token验证失败，因token已经失效，请重新登录");
            }

            Map<String, Object> claims = (Map<String, Object>) jws.getPayload();
            //将数据存入ThreadLocal以供后面的请求使用
            ThreadLocalUtil.set(claims);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);//手动将对象转换为json格式，借助阿里巴巴fastJSON工具依赖
            resp.getWriter().write(notLogin);
            return false;
        }

        //6.放行
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("postHandle...");
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();//清除本次请求的ThreadLocal
//        System.out.println("afterCompletion...");
    }
}
