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
 * CompanyAndRecruit
 *
 * @author lirongqian
 * @since 2018/04/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAndRecruit {

    private Company company;

    private List<Recruit> recruitList;
}