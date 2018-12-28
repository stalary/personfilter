package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.entity.mongodb.Resume;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mongodb.SkillService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ResumeController
 *
 * @description 简历操作接口
 * @author lirongqian
 * @since 2018/04/16
 */
@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    @Resource
    private SkillService skillService;

    /**
     * @method save  保存修改简历
     * @param resume 简历对象
     * @return Resume 简历对象
     **/
    @PostMapping
    public ResponseMessage save(
            @RequestBody Resume resume) {
        return ResponseMessage.successMessage(resumeService.saveResume(resume));
    }

    /**
     * @method getResumeByUserId 通过userId查找对应简历
     * @param userId 用户id
     * @return Resume 简历对象
     **/
    @GetMapping
    public ResponseMessage getResumeByUserId(
            @RequestParam Long userId) {
        return ResponseMessage.successMessage(resumeService.findByUserId(userId));
    }

}