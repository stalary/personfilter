package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.repo.mysql.RecruitRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RecruitService
 *
 * @author lirongqian
 * @since 2018/04/17
 */
@Service
public class RecruitService extends BaseService<Recruit, RecruitRepo> {

    RecruitService(RecruitRepo repo) {
        super(repo);
    }

    public List<Recruit> allRecruit(String key, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        if (StringUtils.isEmpty(key)) {
            List<Recruit> recruitList = repo.findAll(pageRequest).getContent();
            recruitList.forEach(Recruit::deserializeFields);
            return recruitList;
        } else {
            List<Recruit> recruitList = repo.findByContentIsLike("%" + key + "%", pageRequest);
            recruitList.forEach(Recruit::deserializeFields);
            return recruitList;
        }
    }

    public Recruit saveRecruit(Recruit recruit) {
        recruit.serializeFields();
        return repo.save(recruit);
    }
}