package com.stalary.personfilter.repo.mongodb;

import com.stalary.personfilter.data.entity.mongodb.Skill;
import org.springframework.stereotype.Repository;

/**
 * SkillRepo
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Repository
public interface SkillRepo extends BaseRepo<Skill, Long> {

}