/**
 * @(#)CompanyAndRecruit.java, 2018-04-26.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.personfilter.data.vo;

import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @model CompanyAndRecruit
 * @description 公司详情
 * @field company 公司(见Company)
 * @field recruitList 招聘信息列表(见Recruit)
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAndRecruit {

    private Company company;

    private List<Recruit> recruitList;
}