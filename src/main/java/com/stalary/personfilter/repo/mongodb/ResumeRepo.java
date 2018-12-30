package com.stalary.personfilter.repo.mongodb;

import com.stalary.personfilter.data.entity.mongodb.Resume;
import org.springframework.stereotype.Repository;

/**
 * @author Stalary
 * @description
 * @date 2018/4/13
 */
@Repository
public interface ResumeRepo extends BaseRepo<Resume, Long> {

    Resume findByUserId(long userId);
}
