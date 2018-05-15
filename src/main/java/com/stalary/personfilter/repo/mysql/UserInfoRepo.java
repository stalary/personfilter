package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.UserInfo;

/**
 * @author Stalary
 * @description
 * @date 2018/4/22
 */
public interface UserInfoRepo extends BaseRepo<UserInfo, Long> {

    UserInfo findByUserId(Long userId);
}
