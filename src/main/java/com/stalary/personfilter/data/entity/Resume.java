package com.stalary.personfilter.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * Resume
 * 简历实体类
 * @author lirongqian
 * @since 2018/04/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Resume extends BaseEntity {

    /**
     * 关联的用户id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别，男true，女false
     */
    private boolean man;

    /**
     * 年龄
     */
    private int age;

    /**
     * 技能
     */
    @DBRef
    private List<Skill> skills;

    /**
     * 毕业院校
     */
    private String school;

    /**
     * 地址
     */
    private String address;

    /**
     * 毕业时间，仅需要精准到年
     */
    private int endTime;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 自我介绍
     */
    private String introduce;

    /**
     * 个人经历：包括项目经验等
     */
    private String experience;

    /**
     * 奖项
     */
    private String awards;
}