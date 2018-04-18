package com.stalary.personfilter.service.mongodb;

import com.stalary.personfilter.data.vo.ResumeAndUser;
import com.stalary.personfilter.data.entity.mongodb.Skill;
import com.stalary.personfilter.repo.mongodb.ResumeRepo;
import com.stalary.personfilter.repo.mongodb.SkillRepo;
import com.stalary.personfilter.service.WebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SkillService
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Service
public class SkillService extends BaseService<Skill, SkillRepo> {

    public SkillService(SkillRepo repo) {
        super(repo);
    }

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private WebClientService webClientService;

    /**
     * 通过技能点名称查找出技能
     *
     * @param name
     * @return
     */
    private List<Skill> findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongo.find(query, Skill.class);
    }

    /**
     * 通过技能点查找简历
     *
     * @param name
     * @return
     */
    public List<ResumeAndUser> findResumeByName(String name) {
        List<Long> resumeId = findByName(name)
                .stream()
                .map(Skill::getResumeId)
                .collect(Collectors.toList());
        List<ResumeAndUser> list = new ArrayList<>();
        resumeRepo.findAllById(resumeId)
                .forEach(resume -> list.add(new ResumeAndUser(resume, webClientService.getUser(resume.getUserId()))));
        return list;
    }
}