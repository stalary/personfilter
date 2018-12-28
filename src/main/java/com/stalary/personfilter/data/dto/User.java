package com.stalary.personfilter.data.dto;

import lombok.*;

/**
 * @model User
 * @description 用户对象
 * @field userId 用户id
 * @field nickname 昵称
 * @field username 用户名
 * @field password 密码
 * @field companyId 关联的公司Id
 * @field phone 手机号
 * @field email 邮箱
 * @field projectId 项目id
 * @field role 角色，1为hr，2为求职者
 * @field firstId 关联Id
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class User {

    private Long id;

    private String username;

    private String nickname;

    private String phone;

    private String password;

    private String email;

    private Long projectId;

    private Integer role;

    private Long firstId;
}