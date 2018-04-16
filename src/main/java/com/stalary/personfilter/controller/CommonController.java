package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.service.CommonService;
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
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     *  发送短信验证码的接口
     */
    @ApiOperation(value = "验证码", notes = "传入手机号，发送验证码")
    @GetMapping("/code")
    public ResponseMessage code(
            @RequestParam String phone) {
        return ResponseMessage.successMessage(commonService.sendCode(phone));
    }

}