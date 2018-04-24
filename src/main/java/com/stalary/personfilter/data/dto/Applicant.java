package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Applicant
 * 求职者
 * @author lirongqian
 * @since 2018/04/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Applicant {

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
}