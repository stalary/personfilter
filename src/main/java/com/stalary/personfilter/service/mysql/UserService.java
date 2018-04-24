package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.repo.mysql.UserInfoRepo;
import com.stalary.personfilter.service.outer.QiNiuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private QiNiuService qiNiuService;

    public UserInfo getInfo() {
        return repo.findById(UserHolder.get().getId()).orElse(null);
    }

    public void uploadAvatar(MultipartFile avatar) {
        String url = qiNiuService.uploadPicture(avatar);
        UserInfo info = getInfo();
        info.setAvatar(url);
        repo.save(info);
    }

}