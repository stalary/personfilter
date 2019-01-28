package com.stalary.personfilter.service.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.stalary.lightmqclient.facade.Producer;
import com.stalary.personfilter.data.dto.ReceiveResume;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.dto.SkillRule;
import com.stalary.personfilter.data.entity.mongodb.Resume;
import com.stalary.personfilter.data.entity.mongodb.Skill;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.data.vo.ReceiveInfo;
import com.stalary.personfilter.data.vo.SendInfo;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.repo.mongodb.ResumeRepo;
import com.stalary.personfilter.repo.mongodb.SkillRepo;
import com.stalary.personfilter.service.mysql.RecruitService;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.utils.Constant;
import com.stalary.personfilter.utils.IdUtil;
import com.stalary.personfilter.utils.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.stalary.personfilter.utils.Constant.*;

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

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongo;

    @Resource(name = "skillRepo")
    private SkillRepo skillRepo;

    @Resource
    private RecruitService recruitService;

    @Resource
    private Producer producer;

    @Resource
    private UserService userService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redis;


    /**
     * 处理简历
     */
    public void handleResume(SendResume sendResume) {
        log.info("start handle resume");
        HashOperations<String, String, String> redisHash = redis.opsForHash();
        long start = System.currentTimeMillis();
        Long userId = sendResume.getUserId();
        Long recruitId = sendResume.getRecruitId();
        // 构建发送列表
        String sendKey = Constant.getKey(RedisKeys.RESUME_SEND, String.valueOf(userId));
        redisHash.put(sendKey, String.valueOf(recruitId), JSONObject.toJSONString(sendResume));
        // 构建获取列表
        String receiveKey = Constant.getKey(RedisKeys.RESUME_RECEIVE, String.valueOf(recruitId));
        UserInfo userInfo = userService.findOne(userId);
        // 计算匹配度
        int rate = calculate(recruitId, userId);
        ReceiveResume receiveResume = new ReceiveResume(sendResume.getTitle(), userInfo.getNickname(), userInfo.getUserId(), rate, LocalDateTime.now());
        redisHash.put(receiveKey, String.valueOf(userId), JSONObject.toJSONString(receiveResume));
        log.info("end handle resume spend time is " + (System.currentTimeMillis() - start));
    }

    /**
     * 获取投递简历列表
     */
    public SendInfo getSendList() {
        HashOperations<String, String, String> redisHash = redis.opsForHash();
        Long userId = UserHolder.get().getId();
        String sendKey = Constant.getKey(RedisKeys.RESUME_SEND, String.valueOf(userId));
        List<SendResume> ret = new ArrayList<>();
        Map<String, String> entries = redisHash.entries(sendKey);
        entries.forEach((k, v) -> ret.add(JSONObject.parseObject(v, SendResume.class)));
        return new SendInfo(ret);
    }

    /**
     * 获取收到的简历列表
     **/
    public ReceiveInfo getReceiveList() {
        HashOperations<String, String, String> redisHash = redis.opsForHash();
        // 多个岗位需要叠加
        Long userId = UserHolder.get().getId();
        List<Recruit> recruitList = recruitService.findByUserId(userId);
        List<ReceiveResume> ret = new ArrayList<>();
        recruitList.forEach(recruit -> {
            String receiveKey = Constant.getKey(RedisKeys.RESUME_RECEIVE, String.valueOf(recruit.getId()));
            Map<String, String> entries = redisHash.entries(receiveKey);
            entries.forEach((k, v) -> ret.add(JSONObject.parseObject(v, ReceiveResume.class)));
        });
        ret.sort(Comparator.comparing(ReceiveResume::getRate).reversed());
        return new ReceiveInfo(ret);
    }

    /**
     * 保存简历
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
     */
    public int calculate(Long recruitId, Long userId) {
        Recruit recruit = recruitService.findById(recruitId);
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

    /**
     * 投递简历
     */
    public void postResume(Long recruitId, String title) {
        Long userId = UserHolder.get().getId();
        String json = JSONObject.toJSONString(new SendResume(userId, recruitId, title, LocalDateTime.now()));
        // 处理简历
        producer.send(HANDLE_RESUME, json);
        // 向接受方发送通知
        producer.send(RECEIVE_RESUME, json);
        // 向投递方发送通知
        producer.send(SEND_RESUME, json);
    }

}