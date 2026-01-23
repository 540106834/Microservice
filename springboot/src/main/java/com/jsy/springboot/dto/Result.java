package com.jsy.springboot.dto;

public class Result {

    private int code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result r = new Result();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static Result error(String msg) {
        Result r = new Result();
        r.code = 400;
        r.message = msg;
        return r;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}