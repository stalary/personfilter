package com.stalary.personfilter.service.mongodb;

import com.stalary.personfilter.data.dto.SkillRule;
import com.stalary.personfilter.data.entity.mongodb.Resume;
import com.stalary.personfilter.data.entity.mongodb.Skill;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.repo.mongodb.ResumeRepo;
import com.stalary.personfilter.repo.mongodb.SkillRepo;
import com.stalary.personfilter.service.mysql.RecruitService;
import com.stalary.personfilter.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ResumeService
 *
 * @author lirongqian
 * @since 2018/04/14
 */
@Service
@Slf4j
public class ResumeService extends BaseService<Resume, ResumeRepo> {

    public ResumeService(ResumeRepo repo) {
        super(repo);
    }

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private SkillRepo skillRepo;

    @Autowired
    private RecruitService recruitService;

    /**
     * 保存简历
     *
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
                            if (skill.getResumeId() == 0) {
                                skill.setResumeId(resumeId);
                            }
                        })
                        .collect(Collectors.toList())
        );
        return repo.save(resume);
    }

    public Resume findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    /**
     * 简历打分
     *
     * @return
     */
    public int calculate(Long recruitId, Long userId) {
        Recruit recruit = recruitService.findOne(recruitId);
        List<SkillRule> skillRuleList = recruit.getSkillList();
        Resume resume = repo.findByUserId(userId);
        List<Skill> skillList = resume.getSkills();
        // 求出规则表中总和
        int ruleSum = skillRuleList
                .stream()
                .mapToInt(SkillRule::getWeight)
                .sum();
        // 求出规则表中的技能点
        List<String> nameRuleList = skillRuleList
                .stream()
                .map(SkillRule::getName)
                .collect(Collectors.toList());
        // 求出简历表中的技能点
        List<String> nameList = skillList
                .stream()
                .map(Skill::getName)
                .collect(Collectors.toList());
        // 求出技能点交集
        List<String> intersection = nameRuleList
                .stream()
                .filter(nameList::contains)
                .collect(Collectors.toList());
        // 生成规则表的映射
        Map<String, Integer> nameRuleMap = skillRuleList
                .stream()
                .collect(Collectors.toMap(SkillRule::getName, SkillRule::getWeight));
        // 命中的和
        int getRuleSum = intersection
                .stream()
                .mapToInt(nameRuleMap::get)
                .sum();
        // 规则占比
        double rulePercent = (double) getRuleSum / ruleSum;
        // 技能点总和
        int sum = intersection.size() * 4;
        // 生成技能点的映射
        Map<String, Integer> nameMap = skillList
                .stream()
                .collect(Collectors.toMap(Skill::getName, Skill::getLevel));
        // 命中技能点的和
        int getSum = intersection
                .stream()
                .mapToInt(nameMap::get)
                .sum();
        // 技能点占比
        double percent = (double) getSum / sum;
        return (int) Math.round(percent * rulePercent * 100);
    }


}