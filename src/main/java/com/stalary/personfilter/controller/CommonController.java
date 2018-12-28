package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.service.outer.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * CommonController
 *
 * @description 公共操作接口
 * @author lirongqian
 * @since 2018/04/16
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private SmsService smsService;

    /**
     * @method code 发送短信验证码的接口
     * @param phone 手机号
     **/
    @GetMapping("/code")
    public ResponseMessage code(
            @RequestParam String phone) {
        return ResponseMessage.successMessage(smsService.sendCode(phone));
    }

}