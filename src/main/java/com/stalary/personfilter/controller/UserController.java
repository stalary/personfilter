package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.Applicant;
import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.redis.RedisKeys;
import com.stalary.personfilter.service.redis.RedisService;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author lirongqian
 * @since 2018/04/13
 */
@Api(tags = "用户操作接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private WebClientService webClientService;

    @Autowired
    private RedisService redisService;

    /**
     * 求职者注册
     */
    @PostMapping
    @ApiOperation(value = "求职者注册",notes = "传入求职者注册对象")
    public ResponseMessage register(
            @RequestBody Applicant applicant) {
        return webClientService.postUser(applicant, Constant.REGISTER);
    }

    /**
     * 求职者登陆，仅需要用户名和密码
     */
    @PostMapping("/login")
    @ApiOperation(value = "求职者登陆", notes = "传入求职者登陆对象")
    public ResponseMessage login(
            @RequestBody Applicant applicant) {
        return webClientService.postUser(applicant, Constant.LOGIN);
    }

    /**
     * hr注册
     */
    @PostMapping("/hr")
    @ApiOperation(value = "hr注册",notes = "传入hr注册对象")
    public ResponseMessage hrRegister(
            @RequestBody HR hr) {
        return webClientService.postUser(hr, Constant.REGISTER);
    }

    /**
     * hr登陆，仅需要用户名和密码
     */
    @PostMapping("/hr/login")
    @ApiOperation(value = "hr登陆", notes = "传入hr登陆对象")
    public ResponseMessage hrLogin(
            @RequestBody HR hr) {
        return webClientService.postUser(hr, Constant.LOGIN);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    @LoginRequired
    @ApiOperation(value = "退出登陆", notes = "传入token")
    public ResponseMessage logout(
            @RequestParam String token) {
        if (redisService.remove(RedisKeys.getKey(RedisKeys.USER_TOKEN, token))) {
            return ResponseMessage.successMessage("退出成功");
        }
        return ResponseMessage.failedMessage("退出失败");
    }

    /**
     * 获得用户信息，header中带入token
     * @return
     */
    @GetMapping
    @ApiOperation(value = "获得用户信息", notes = "header中带入token")
    @LoginRequired
    public ResponseMessage getInfo() {
        log.info("info: " + UserHolder.get());
        return ResponseMessage.successMessage(UserHolder.get());
    }

    /**
     * 通过手机号修改求职者密码
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改求职者密码", notes = "通过手机号进行修改")
    public ResponseMessage update(
            @RequestBody Applicant applicant) {
        return webClientService.postUser(applicant, Constant.UPDATE);
    }

    /**
     * 通过手机号修改hr密码
     */
    @PutMapping
    @ApiOperation(value = "修改hr密码", notes = "通过手机号进行修改")
    public ResponseMessage hrUpdate(
            @RequestBody HR hr) {
        return webClientService.postUser(hr, Constant.UPDATE);
    }

    @GetMapping("/token")
    @ApiOperation(value = "使用token获取用户信息", notes = "传入token")
    public ResponseMessage token(
            @RequestParam String token) {
        return ResponseMessage.successMessage(webClientService.getUser(token));
    }
}