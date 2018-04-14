package com.stalary.personfilter.service.mongodb;

import com.stalary.personfilter.data.entity.mongodb.Resume;
import com.stalary.personfilter.repo.mongodb.ResumeRepo;
import com.stalary.personfilter.repo.mongodb.SkillRepo;
import com.stalary.personfilter.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * ResumeService
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Service
public class ResumeService extends BaseService<Resume, ResumeRepo> {

    public ResumeService(ResumeRepo repo) {
        super(repo);
    }

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private SkillRepo skillRepo;

    /**
     * 保存简历
     * @param resume
     * @return
     */
    public Resume saveResume(Resume resume) {
        final long resumeId = IdUtil.getNextId(Resume.class.getSimpleName(), mongo);
        skillRepo.saveAll(
                resume.getSkills()
                .stream()
                .peek(skill -> {
                    // 存入简历id
                    skill.setResumeId(resumeId);
                })
                .collect(Collectors.toList())
        );
        return repo.save(resume);
    }

}