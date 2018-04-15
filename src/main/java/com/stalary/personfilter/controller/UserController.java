package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author lirongqian
 * @since 2018/04/13
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private WebClientService webClientService;

    /**
     * 注册
     */
    @PostMapping
    public ResponseMessage register(
            @RequestBody User user) {
        return webClientService.postUser(user, Constant.REGISTER);
    }

    /**
     * 登陆，仅需要用户名和密码
     */
    @PostMapping("/login")
    public ResponseMessage login(
            @RequestBody User user) {
        return webClientService.postUser(user, Constant.LOGIN);
    }
}