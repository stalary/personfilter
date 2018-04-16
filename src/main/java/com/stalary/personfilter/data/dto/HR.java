package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HR
 * hr
 * @author lirongqian
 * @since 2018/04/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HR {

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 关联的公司Id
     */
    private Long companyId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}