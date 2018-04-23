package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.repo.mysql.UserInfoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserService
 *
 * @author lirongqian
 * @since 2018/04/22
 */
@Service
@Slf4j
public class UserService extends BaseService<UserInfo, UserInfoRepo> {

    UserService(UserInfoRepo repo) {
        super(repo);
    }

    public UserInfo getInfo() {
        return repo.findById(UserHolder.get().getId()).orElse(null);
    }
}