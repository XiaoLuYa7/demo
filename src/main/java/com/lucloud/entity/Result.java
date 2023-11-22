package com.lucloud.entity;

import lombok.Data;

/**
 *
 * @author Max
 * @since 2020/1/15
 */
@Data
public class Result {
    private Integer code = 100;
    private String msg = "";
    private Object data;

    public Result (){
    }

    public Result (Integer code){
        this.code = code;
    }

    public Result (Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code,String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result error(String msg){
        return new Result(-1,msg);
    }

    public static Result success(Object data){
        return new Result(1,"success",data);
    }

    public Boolean checkIsOk(){
        return code != null && code == 1;
    }

    public static Result ok() {
        return new Result(1, null, null);
    }

    public static Result ok(Object obj) {
        return new Result(1, null, obj);
    }
}
