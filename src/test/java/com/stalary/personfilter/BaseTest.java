package com.stalary.personfilter; /**
 * @(#)BaseTest.java, 2018-10-20.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import com.stalary.personfilter.service.mysql.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


/**
 * BaseTest
 *
 * @author lirongqian
 * @since 2018/10/20
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @Resource
    private CompanyService companyService;

    @Test
    public void test() {
        System.out.println(companyService.getInfo(2L));
    }
}