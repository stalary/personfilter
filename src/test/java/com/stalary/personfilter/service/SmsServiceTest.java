/**
 * @(#)SkillServiceTest.java, 2018-12-29.
 *
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.personfilter.service;

import com.stalary.personfilter.BaseTest;
import com.stalary.personfilter.service.outer.SmsService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * SmsServiceTest
 *
 * @author lirongqian
 * @since 2018/12/29
 */
public class SmsServiceTest extends BaseTest {

    @Resource
    private SmsService smsService;

    @Test
    public void test() {
        smsService.sendCode("17853149599");
    }
}