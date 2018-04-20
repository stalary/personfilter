package com.stalary.personfilter.service.redis;

import com.google.common.base.Joiner;

import static com.stalary.personfilter.utils.Constant.SPLIT;

/**
 * RedisKeys
 *
 * @author lirongqian
 * @since 2018/04/20
 */
public class RedisKeys {


    public static final String PROJECT_INFO = "pf_project_info";

    public static final String USER_TOKEN = "pf_user_token";

    public static final Joiner JOINER = Joiner.on(SPLIT);

    public static String getKey(String... keys) {
        return JOINER.join(keys);
    }
}