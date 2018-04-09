package com.stalary.personfilter.controller;

import com.stalary.personfilter.data.dto.ResponseMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public ResponseMessage hello() {
        return ResponseMessage.successMessage("hello world");
    }
}