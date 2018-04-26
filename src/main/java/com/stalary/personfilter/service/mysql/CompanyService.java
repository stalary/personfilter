package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.vo.CompanyAndRecruit;
import com.stalary.personfilter.repo.mysql.CompanyRepo;
import com.stalary.personfilter.repo.mysql.RecruitRepo;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CompanyService
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Service
public class CompanyService extends BaseService<Company, CompanyRepo> {

    CompanyService(CompanyRepo repo) {
        super(repo);
    }

    @Autowired
    private RecruitRepo recruitRepo;

    public Pair<List<Company>, Integer> allCompany(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Company> companyList = repo.findAll(pageRequest);
        return new Pair<>(companyList.getContent(), companyList.getTotalPages());
    }

    public List<Company> findCompany(String key) {
        if (StringUtils.isEmpty(key)) {
            return repo.findAll();
        } else {
            return repo.findByNameIsLike("%" + key + "%");
        }
    }

    public CompanyAndRecruit getInfo(Long id) {
        Company company = findOne(id);
        List<Recruit> recruitList = recruitRepo.findByCompanyId(id);
        return new CompanyAndRecruit(company, recruitList);
    }

}