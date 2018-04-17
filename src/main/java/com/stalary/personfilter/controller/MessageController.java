package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.Message;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mysql.MessageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * MessageController
 *
 * @author lirongqian
 * @since 2018/04/17
 */
@Api(tags = "站内信以及邮件接口")
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 推送一条站内信
     * @return
     */
    @PostMapping
    @LoginRequired
    public ResponseMessage postMessage(
            @RequestBody Message message) {
        log.info("message: " + message);
        return ResponseMessage.successMessage(messageService.save(message));
    }

    /**
     * 获取一个用户接收的站内信
     */
    @GetMapping("/user/get")
    @LoginRequired
    public ResponseMessage getMessage() {
        User user = UserHolder.get();
        if (user != null) {
            return ResponseMessage.successMessage(messageService.findByToId(user.getId()));
        } else {
            return ResponseMessage.successMessage();
        }
    }

    /**
     * 获取一个用户发送的站内信
     */
    @GetMapping("/user/send")
    @LoginRequired
    public ResponseMessage getSendMessage(
            @RequestParam Long userId) {
        return ResponseMessage.successMessage(messageService.findByFromId(userId));
    }


}