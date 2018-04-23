package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.Applicant;
import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.outer.GoEasyService;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.service.outer.QiNiuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.stalary.personfilter.utils.Constant.*;

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
    private UserService userService;

    @Autowired
    private QiNiuService qiNiuService;

    /**
     * 求职者注册
     */
    @PostMapping
    @ApiOperation(value = "求职者注册", notes = "传入求职者注册对象")
    public ResponseMessage register(
            @RequestBody Applicant applicant) {
        return webClientService.postUser(applicant, REGISTER);
    }

    /**
     * 求职者登陆，仅需要用户名和密码
     */
    @PostMapping("/login")
    @ApiOperation(value = "求职者登陆", notes = "传入求职者登陆对象")
    public ResponseMessage login(
            @RequestBody Applicant applicant) {
        return webClientService.postUser(applicant, LOGIN);
    }

    /**
     * hr注册
     */
    @PostMapping("/hr")
    @ApiOperation(value = "hr注册", notes = "传入hr注册对象")
    public ResponseMessage hrRegister(
            @RequestBody HR hr) {
        return webClientService.postUser(hr, REGISTER);
    }

    /**
     * hr登陆，仅需要用户名和密码
     */
    @PostMapping("/hr/login")
    @ApiOperation(value = "hr登陆", notes = "传入hr登陆对象")
    public ResponseMessage hrLogin(
            @RequestBody HR hr) {
        return webClientService.postUser(hr, LOGIN);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    @LoginRequired
    @ApiOperation(value = "退出登陆", notes = "传入token")
    public ResponseMessage logout() {
        return ResponseMessage.failedMessage("退出成功");
    }

    /**
     * 获得用户信息，header中带入token
     *
     * @return
     */
    @GetMapping
    @ApiOperation(value = "获得用户信息", notes = "header中带入token")
    @LoginRequired
    public ResponseMessage getInfo() {
        return ResponseMessage.successMessage(userService.getInfo());
    }

    @PutMapping("/info")
    @ApiOperation(value = "修改个人信息", notes = "个人信息对象")
    @LoginRequired
    public ResponseMessage updateInfo(
            @RequestBody UserInfo userInfo) {
        User user = UserHolder.get();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        return ResponseMessage.successMessage(userService.save(userInfo));
    }

    @PutMapping("/phone")
    @ApiOperation(value = "修改手机号", notes = "手机号")
    @LoginRequired
    public ResponseMessage updatePhone(
            @RequestParam String phone) {
        User user = UserHolder.get();
        user.setPhone(phone);
        return webClientService.postUser(user, UPDATE);
    }

    /**
     * 通过手机号修改hr密码
     */
    @PutMapping("/password")
    @ApiOperation(value = "修改密码", notes = "通过用户名和手机号和新密码进行修改")
    public ResponseMessage updatePassword(
            @RequestParam String username,
            @RequestParam String phone,
            @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        return webClientService.postUser(user, UPDATE_PASSWORD);
    }

    @PutMapping("/email")
    @ApiOperation(value = "修改邮箱", notes = "传入新邮箱")
    @LoginRequired
    public ResponseMessage updateEmail(
            @RequestParam String email) {
        User user = UserHolder.get();
        user.setEmail(email);
        return webClientService.postUser(user, UPDATE);
    }

    @GetMapping("/token")
    @ApiOperation(value = "使用token获取用户信息", notes = "传入token")
    public ResponseMessage token(
            @RequestParam String token) {
        return ResponseMessage.successMessage(webClientService.getUser(token));
    }

    @PostMapping("/avatar")
    @ApiOperation(value = "上传用户头像", notes = "传入图片")
    @LoginRequired
    public ResponseMessage upload(
            @RequestParam("avatar") MultipartFile avatar) {
        return ResponseMessage.successMessage(qiNiuService.uploadPicture(avatar));
    }
}