package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String message;  //响应信息 描述字符串
    private T data; //返回的数据

    public static <E> Result<E> success(E data){
        return new Result<>(0,"success", data);
    }
    public static Result success(){
        return new Result(0, "success",null);
    }
    public static Result error(String msg){
        return new Result(1, msg,null);
    }
}
