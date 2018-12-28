package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/4/17
 */
@Repository
public interface RecruitRepo extends BaseRepo<Recruit, Long> {

    Page<Recruit> findByTitleIsLike(String key, Pageable pageable);

    List<Recruit> findByHrId(Long userId);

    List<Recruit> findByCompanyId(Long id);
}
