package com.stalary.personfilter.controller;

import com.stalary.lightmqclient.facade.Producer;
import com.stalary.personfilter.data.entity.mysql.Company;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.service.ClientService;
import com.stalary.personfilter.service.WebSocketService;
import com.stalary.personfilter.service.mongodb.ResumeService;
import com.stalary.personfilter.service.mysql.CompanyService;
import com.stalary.personfilter.service.outer.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * TestController
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@RestController
public class TestController {

    @Resource
    private ClientService clientService;

    @Value("${server.user}")
    private String userCenterServer;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redis;

    @Resource
    private CompanyService companyService;

    @Resource
    private Producer producer;

    @Resource
    private ResumeService resumeService;

    @Resource
    private MailService mailService;

    @Resource
    private WebSocketService webSocketService;

    @GetMapping("/hello")
    public ResponseMessage hello() {
        clientService.getProjectInfo();
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
        producer.send("test", message);
        return ResponseMessage.successMessage();
    }

    @GetMapping("/websocket")
    public ResponseMessage websocket(
            @RequestParam Long userId,
            @RequestParam String message) {
        webSocketService.sendMessage(userId, message);
        return ResponseMessage.successMessage();
    }

    @GetMapping("/calculate")
    public ResponseMessage calculate(
            @RequestParam Long userId,
            @RequestParam Long recruitId) {
        return ResponseMessage.successMessage(resumeService.calculate(recruitId, userId));
    }

    @GetMapping("/mail")
    public ResponseMessage mail() {
        mailService.sendResume("stalary@163.com", "测试邮件");
        return ResponseMessage.successMessage();
    }

}