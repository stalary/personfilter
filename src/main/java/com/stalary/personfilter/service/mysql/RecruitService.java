package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.vo.RecruitAndCompany;
import com.stalary.personfilter.data.vo.RecruitAndHrAndCompany;
import com.stalary.personfilter.repo.mysql.RecruitRepo;
import com.stalary.personfilter.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private ClientService clientService;

    @Resource
    private CompanyService companyService;

    public Map<String, Object> allRecruit(String key, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<RecruitAndCompany> list = new ArrayList<>();
        List<Recruit> recruitList;
        Page<Recruit> recruitPage;
        if (StringUtils.isEmpty(key)) {
            recruitPage = repo.findAll(pageRequest);
            recruitList = recruitPage.getContent();
        } else {
            recruitPage = repo.findByTitleIsLike("%" + key + "%", pageRequest);
            recruitList = recruitPage.getContent();
        }
        recruitList.forEach(recruit -> {
            recruit.deserializeFields();
            list.add(new RecruitAndCompany(recruit, companyService.findOne(recruit.getCompanyId())));
        });
        Map<String, Object> result = new HashMap<>();
        result.put("total", recruitPage.getTotalPages());
        result.put("recruitList", list);
        return result;
    }

    public RecruitAndHrAndCompany getRecruitInfo(Long id) {
        Recruit recruit = findById(id);
        User user = clientService.getUser(recruit.getHrId());
        HR hr = new HR().toBuilder()
                .companyId(recruit.getCompanyId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .username(user.getUsername()).build();
        Company company = companyService.findOne(recruit.getCompanyId());
        return new RecruitAndHrAndCompany(recruit, hr, company);
    }

    public Recruit saveRecruit(Recruit recruit) {
        recruit.serializeFields();
        Recruit save = repo.save(recruit);
        save.deserializeFields();
        return save;
    }

    public List<Recruit> findByUserId(Long userId) {
        List<Recruit> recruitList = repo.findByHrId(userId);
        recruitList.forEach(Recruit::deserializeFields);
        return recruitList;
    }


    public Recruit findById(Long id) {
        Recruit recruit = findOne(id);
        recruit.deserializeFields();
        return recruit;
    }
}