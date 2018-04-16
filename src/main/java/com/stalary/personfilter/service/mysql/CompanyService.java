package com.stalary.personfilter.service.mysql;

import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.repo.mysql.CompanyRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<Company> allCompany(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return repo.allCompany(pageRequest);
    }

    public List<Company> allCompany() {
        return repo.findAll();
    }

}