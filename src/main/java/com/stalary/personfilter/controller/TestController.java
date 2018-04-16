package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mongodb.SkillService;
import com.stalary.personfilter.service.mysql.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * TestController
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@RestController
@ApiIgnore
public class TestController {

    @Autowired
    private WebClientService webClientService;

    @Value("${server.user}")
    private String userCenterServer;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/hello")
    public ResponseMessage hello() {
        webClientService.getProjectInfo();
        return ResponseMessage.successMessage(userCenterServer);
    }

    @GetMapping("/projectInfo")
    public ResponseMessage projectInfo() {
        return ResponseMessage.successMessage(redis.opsForValue().get("project"));
    }

    @RequestMapping("/mongodb")
    public ResponseMessage mongodb(
            @RequestParam String name) {
        return ResponseMessage.successMessage(skillService.findResumeByName(name));
    }

    @RequestMapping("/mysql")
    public ResponseMessage mysql(
            @RequestBody Company company) {
        return ResponseMessage.successMessage(companyService.save(company));
    }
}