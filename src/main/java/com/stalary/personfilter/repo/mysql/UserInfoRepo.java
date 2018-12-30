package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Stalary
 * @description
 * @date 2018/4/22
 */
@Repository
public interface UserInfoRepo extends BaseRepo<UserInfo, Long> {

    UserInfo findByUserId(Long userId);
}
