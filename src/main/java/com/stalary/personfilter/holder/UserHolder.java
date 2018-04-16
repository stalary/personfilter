package com.stalary.personfilter.holder;

import com.stalary.personfilter.data.dto.User;

/**
 * UserHolder
 *
 * @author lirongqian
 * @since 2018/04/16
 */
public class UserHolder {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static User get() {
        return userThreadLocal.get();
    }

    public static void set(User projectInfo) {
        userThreadLocal.set(projectInfo);
    }

    public static void remove() {
        userThreadLocal.remove();
    }
}