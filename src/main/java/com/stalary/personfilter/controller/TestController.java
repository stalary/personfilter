package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.dto.Skill;
import com.stalary.personfilter.data.entity.Resume;
import com.stalary.personfilter.repo.ResumeRepo;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.utils.PFUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TestController
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@RestController
public class TestController {

    @Resource
    private WebClientService webClientService;

    @Value("${server.user}")
    private String userCenterServer;

    @Resource
    private ResumeRepo resumeRepo;

    @Resource
    private StringRedisTemplate redis;

    @GetMapping("/hello")
    public ResponseMessage hello() {
        webClientService.getProjectInfo();
        return ResponseMessage.successMessage(userCenterServer);
    }

    @GetMapping("/projectInfo")
    public ResponseMessage projectInfo() {
        return ResponseMessage.successMessage(redis.opsForValue().get("project"));
    }

    @GetMapping("/mongodb")
    public ResponseMessage mongodb() {
        return ResponseMessage.successMessage(resumeRepo.findAll());
    }
}