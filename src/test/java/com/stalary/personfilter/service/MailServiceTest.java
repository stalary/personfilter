/**
 * @(#)MailServiceTest.java, 2018-12-30.
 *
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.personfilter.service;

import com.stalary.personfilter.BaseTest;
import com.stalary.personfilter.service.outer.MailService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * MailServiceTest
 *
 * @author lirongqian
 * @since 2018/12/30
 */
public class MailServiceTest extends BaseTest {

    @Resource
    private MailService mailService;

    @Test
    public void test() {
        mailService.sendEmail("452024236@qq.com", "测试", "测试邮件");
    }
}