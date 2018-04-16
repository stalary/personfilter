package com.stalary.personfilter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User
 *
 * @author lirongqian
 * @since 2018/04/15
 */
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户名
     */
    @Getter
    private String username;

    /**
     * 昵称
     */
    @Getter
    private String nickname;

    /**
     * 手机号
     */
    @Getter
    private String phone;

    /**
     * 密码
     */
    @Getter
    private String password;

    /**
     * 邮箱
     */
    @Getter
    private String email;

    /**
     * 项目id
     */
    @Getter
    private Long projectId;

    /**
     * 角色，1为hr，2为求职者
     */
    @Getter
    private Integer role;

    /**
     * 关联Id
     */
    @Getter
    private Long firstId;

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public User setRole(Integer role) {
        this.role = role;
        return this;
    }

    public User setFirstId(Long firstId) {
        this.firstId = firstId;
        return this;
    }
}