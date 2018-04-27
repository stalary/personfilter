/**
 * @(#)RecruitAndCompany.java, 2018-04-27.
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

/**
 * RecruitAndCompany
 *
 * @author lirongqian
 * @since 2018/04/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitAndCompany {

    private Recruit recruit;

    private Company company;
}