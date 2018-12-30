/**
 * @(#)KafkaServiceTest.java, 2018-12-30.
 *
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.personfilter.service;

import com.stalary.lightmqclient.facade.Producer;
import com.stalary.personfilter.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * KafkaServiceTest
 *
 * @author lirongqian
 * @since 2018/12/30
 */
public class KafkaServiceTest extends BaseTest {

    @Resource
    private Producer producer;

    @Test
    public void test() {
        System.out.println(producer.send("test", "test1"));
    }
}