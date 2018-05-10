package com.stalary.personfilter.holder;

import com.stalary.personfilter.data.dto.User;

/**
 * TokenHolder
 *
 * @author lirongqian
 * @since 2018/05/10
 */
public class TokenHolder {

    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    public static String get() {
        return tokenThreadLocal.get();
    }

    public static void set(String token) {
        tokenThreadLocal.set(token);
    }

    public static void remove() {
        tokenThreadLocal.remove();
    }
}