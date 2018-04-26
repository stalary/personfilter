package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.service.kafka.Producer;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mysql.CompanyService;
import com.stalary.personfilter.service.outer.GoEasyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.stalary.personfilter.utils.Constant.NOTIFY;

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
    private StringRedisTemplate redis;


    @Autowired
    private CompanyService companyService;

    @Autowired
    private GoEasyService commonService;

    @Autowired
    private Producer producer;

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/hello")
    public ResponseMessage hello() {
        webClientService.getProjectInfo();
        return ResponseMessage.successMessage(userCenterServer);
    }

    @GetMapping("/projectInfo")
    public ResponseMessage projectInfo() {
        return ResponseMessage.successMessage(redis.opsForValue().get("project"));
    }

    @PostMapping("/mysql")
    public ResponseMessage mysql(
            @RequestBody Company company) {
        return ResponseMessage.successMessage(companyService.save(company));
    }

    @GetMapping("/kafka")
    public ResponseMessage kafka(
            @RequestParam String message) {
        producer.send(NOTIFY, message);
        return ResponseMessage.successMessage();
    }

    @GetMapping("/goeasy")
    public ResponseMessage goeasy() {
        commonService.pushMessage("test", "123");
        return ResponseMessage.successMessage();
    }

    @GetMapping("/calculate")
    public ResponseMessage calculate(
            @RequestParam Long userId,
            @RequestParam Long recruitId) {
        return ResponseMessage.successMessage(resumeService.calculate(recruitId, userId));
    }

}