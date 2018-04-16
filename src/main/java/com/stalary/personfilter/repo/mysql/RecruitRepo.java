package com.stalary.personfilter.repo.mysql;

import com.stalary.personfilter.data.entity.mysql.Recruit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/4/17
 */
public interface RecruitRepo extends BaseRepo<Recruit, Long> {

    @Query("select r from Recruit r")
    List<Recruit> allRecruit(Pageable pageable);

    @Query("select r from Recruit r where r.content like ?1")
    List<Recruit> findRecruitListByKey(String key, Pageable pageable);
}
