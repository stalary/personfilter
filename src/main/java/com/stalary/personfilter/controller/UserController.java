package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.Applicant;
import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mysql.MessageService;
import com.stalary.personfilter.service.outer.GoEasyService;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.service.outer.QiNiuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kotlin.UseExperimental;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
    private MessageService messageService;

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
     * 登陆，仅需要用户名和密码
     */
    @PostMapping("/login")
    @ApiOperation(value = "登陆", notes = "传入登陆对象，仅需要用户名和密码")
    public ResponseMessage login(
            @RequestBody User user) {
        ResponseMessage responseMessage = webClientService.postUser(user, LOGIN);
        if (!responseMessage.isSuccess()) {
            return responseMessage;
        }
        String token = responseMessage.getData().toString();
        User getUser = webClientService.getUser(token);
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", token);
        map.put("role", getUser.getRole());
        map.put("userId", getUser.getId());
        map.put("companyId", getUser.getFirstId());
        messageService.getCount();
        return ResponseMessage.successMessage(map);
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

    /**
     * 修改手机号
     * @param params
     * @return
     */
    @PutMapping("/phone")
    @ApiOperation(value = "修改手机号", notes = "手机号")
    @LoginRequired
    public ResponseMessage updatePhone(
            @RequestBody Map<String, String> params) {
        User user = UserHolder.get();
        user.setPhone(params.get("phone"));
        return webClientService.postUser(user, UPDATE);
    }

    /**
     * 通过手机号修改密码
     */
    @PutMapping("/password")
    @ApiOperation(value = "修改密码", notes = "通过新密码进行修改")
    @LoginRequired
    public ResponseMessage updatePassword(
            @RequestBody Map<String, String> params) {
        User user = UserHolder.get();
        user.setPassword(params.get("password"));
        return webClientService.postUser(user, UPDATE_PASSWORD);
    }

    /**
     * 忘记密码
     */
    @PostMapping("/password")
    @ApiOperation(value = "忘记密码", notes = "通过用户名，手机号，新密码进行修改")
    public ResponseMessage forgetPassword(
            @RequestBody Map<String, String> params) {
        User user = new User();
        user.setPassword(params.get("password"));
        user.setUsername(params.get("username"));
        user.setPhone(params.get("phone"));
        return webClientService.postUser(user, UPDATE_PASSWORD);
    }

    /**
     * 修改邮箱
     * @param params
     * @return
     */
    @PutMapping("/email")
    @ApiOperation(value = "修改邮箱", notes = "传入新邮箱")
    @LoginRequired
    public ResponseMessage updateEmail(
            @RequestBody Map<String, String> params) {
        User user = UserHolder.get();
        user.setEmail(params.get("email"));
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
        userService.uploadAvatar(avatar);
        return ResponseMessage.successMessage("头像上传成功");
    }
}