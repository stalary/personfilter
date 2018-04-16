package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.entity.mongodb.Resume;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mongodb.SkillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ResumeController
 *
 * @author lirongqian
 * @since 2018/04/16
 */
@Api(tags = "简历操作接口")
@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private SkillService skillService;

    @PostMapping
    @ApiOperation(value = "保存修改简历", notes = "传入简历对象")
    public ResponseMessage save(
            @RequestBody Resume resume) {
        return ResponseMessage.successMessage(resumeService.saveResume(resume));
    }

    /**
     * 通过技能点查找简历
     * @param name
     * @return
     */
    @GetMapping
    @ApiOperation(value = "通过技能点查找简历和对应的人", notes = "传入技能名称")
    public ResponseMessage getResumeByName(
            @RequestParam String name) {
        return ResponseMessage.successMessage(skillService.findResumeByName(name));
    }
}