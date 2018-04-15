package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 *
 * @author lirongqian
 * @since 2018/04/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 项目id，对前端隐藏
     */
    private Long projectId;
}