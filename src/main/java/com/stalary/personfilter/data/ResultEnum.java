package com.stalary.personfilter.data;

import lombok.Getter;

/**
 * @author Stalary
 * @description
 * @date 2018/03/24
 */
public enum ResultEnum {
    UNKNOW_ERROR(-1000, "未知错误"),

    // 1开头为用户有关的错误
    NEED_LOGIN(1001, "未登陆"),
    PASSWORD_EMPTY(1002, "密码为空"),
    USERNAME_PASSWORD_ERROR(1003, "账号密码错误"),
    USERNAME_REPEAT(1004, "该用户已注册"),
    UPDATE_PASSWORD_ERROR(1005, "信息不足，无法修改密码"),
    TICKET_EXPIRED(1006, "凭证失效，请重新登陆"),

    // 2开头为项目有关的错误
    PROJECT_REJECT(2003, "密钥错误"),
    PROJECT_REPEAT(2002, "该用户已注册"),
    PROJECT_ERROR(2003, "项目名错误"),

    SUCCESS(0, "成功");

    @Getter
    private Integer code;

    @Getter
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
