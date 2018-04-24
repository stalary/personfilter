package com.stalary.personfilter.utils;

import org.apache.tomcat.util.bcel.Const;

import java.util.UUID;

/**
 * PFUtil
 *
 * @author lirongqian
 * @since 2018/04/17
 */
public class PFUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    public static String getPicture() {
        return Constant.getKey(Constant.PICTURE, getUUID());
    }
}