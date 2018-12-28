package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @model HR
 * @description 招聘者对象
 * @field userId 用户id
 * @field nickname 姓名
 * @field username 用户名
 * @field password 密码
 * @field companyId 关联的公司Id
 * @field phone 手机号
 * @field email 邮箱
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HR {

    private Long userId;

    private String nickname;

    private String username;

    private String password;

    private Long companyId;

    private String phone;

    private String email;
}