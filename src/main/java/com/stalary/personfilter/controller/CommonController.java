package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.service.outer.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CommonController
 *
 * @author lirongqian
 * @since 2018/04/16
 */
@Api(tags = "公共操作接口")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SmsService smsService;

    /**
     *  发送短信验证码的接口
     */
    @ApiOperation(value = "验证码", notes = "传入手机号，发送验证码")
    @GetMapping("/code")
    public ResponseMessage code(
            @RequestParam String phone) {
        return ResponseMessage.successMessage(smsService.sendCode(phone));
    }

}