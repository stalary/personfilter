package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/4/17
 */
public interface RecruitRepo extends BaseRepo<Recruit, Long> {

    Page<Recruit> findByContentIsLike(String key, Pageable pageable);

    List<Recruit> findByHrId(Long userId);
}
