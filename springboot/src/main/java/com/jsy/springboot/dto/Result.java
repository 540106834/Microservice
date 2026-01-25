package com.jsy.springboot.dto;

public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data);
    }

    // 失败
    public static <T> Result<T> error(String message) {
        return new Result<>(-1, message, null);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}