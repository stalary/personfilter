package com.stalary.personfilter.data.entity.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * UserInfo
 * 与用户关联的用户信息表
 * @author lirongqian
 * @since 2018/04/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "UserInfo")
@Entity
public class UserInfo extends BaseEntity {


    /**
     * 关联的userId
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     *  昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Boolean man;

    /**
     * 个人介绍
     */
    private String introduce;

    /**
     * 地址
     */
    private String address;

    /**
     * 意向公司
     */
    private String intentionCompany;

    /**
     * 毕业时间
     */
    private Integer endTime;

    /**
     * 毕业学校
     */
    private String school;

    /**
     * 学历
     */
    private String education;

    /**
     * 意向岗位
     */
    private String intentionJob;

    /**
     * 头像
     */
    private String avatar;
}