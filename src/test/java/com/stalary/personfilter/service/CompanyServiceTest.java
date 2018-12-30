/**
 * @(#)CommpanyServiceTest.java, 2018-12-30.
 *
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.personfilter.service;

import com.stalary.personfilter.BaseTest;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.vo.CompanyAndRecruit;
import com.stalary.personfilter.service.mysql.CompanyService;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * CommpanyServiceTest
 *
 * @author lirongqian
 * @since 2018/12/30
 */
public class CompanyServiceTest extends BaseTest {

    @Resource
    private CompanyService companyService;

    @Test
    public void allCompany() {
        Map<String, Object> stringObjectMap = companyService.allCompany(1, 4);
        List<Company> companyList = (List<Company>) stringObjectMap.get("companyList");
        System.out.println(companyList);
        Assert.assertEquals(4, companyList.size());
    }

    @Test
    public void findCompany() {
        List<Company> companyList = companyService.findCompany("网易");
        System.out.println(companyList);
        Assert.assertTrue(!companyList.isEmpty());
    }

    @Test
    public void getInfo() {
        CompanyAndRecruit info = companyService.getInfo(1L);
        System.out.println(info);
        Assert.assertNotNull(info);
    }
}