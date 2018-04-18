package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.RecruitAndHrAndCompany;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.repo.mysql.RecruitRepo;
import com.stalary.personfilter.service.WebClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * RecruitService
 *
 * @author lirongqian
 * @since 2018/04/17
 */
@Service
@Slf4j
public class RecruitService extends BaseService<Recruit, RecruitRepo> {

    RecruitService(RecruitRepo repo) {
        super(repo);
    }

    @Autowired
    private WebClientService webClientService;

    @Autowired
    private CompanyService companyService;

    public List<RecruitAndHrAndCompany> allRecruit(String key, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<RecruitAndHrAndCompany> list = new ArrayList<>();
        List<Recruit> recruitList;
        if (StringUtils.isEmpty(key)) {
            recruitList = repo.findAll(pageRequest).getContent();
        } else {
            recruitList = repo.findByContentIsLike("%" + key + "%", pageRequest);
        }
        recruitList.forEach(recruit -> {
            recruit.deserializeFields();
            User user = webClientService.getUser(recruit.getHrId());
            HR hr = new HR()
                    .setCompanyId(recruit.getCompanyId())
                    .setEmail(user.getEmail())
                    .setNickname(user.getNickname())
                    .setPhone(user.getPhone())
                    .setUsername(user.getUsername());
            list.add(new RecruitAndHrAndCompany(recruit, hr, companyService.findOne(recruit.getCompanyId())));
        });
        return list;
    }

    public Recruit saveRecruit(Recruit recruit) {
        recruit.serializeFields();
        return repo.save(recruit);
    }
}