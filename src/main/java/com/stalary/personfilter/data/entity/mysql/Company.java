package com.stalary.personfilter.data.entity.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Company
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "company")
@Entity
public class Company extends BaseEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 图片
     */
    private String avatar;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 规模
     */
    private String scale;

    /**
     * 类型
     */
    private String type;
}