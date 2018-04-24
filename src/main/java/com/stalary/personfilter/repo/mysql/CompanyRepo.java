package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/4/14
 */
public interface CompanyRepo extends BaseRepo<Company, Long> {

    List<Company> findByNameIsLike(String key);
}
