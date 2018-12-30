package com.stalary.personfilter.controller;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.dto.ReadMessage;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.Message;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mysql.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * MessageController
 * @description 站内信以及邮件接口
 * @author lirongqian
 * @since 2018/04/17
 */
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * @method postMessage 推送一条站内信，可以供前端群发站内信使用
     * @param message 站内信
     * @return Message 站内信
     **/
    @PostMapping
    public ResponseMessage postMessage(
            @RequestBody Message message) {
        log.info("message: " + message);
        return ResponseMessage.successMessage(messageService.save(message));
    }

    /**
     * @method getMessage 获取一个用户接收的站内信
     * @return Message 站内信
     **/
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
     * @method readMessage 已读站内信
     * @param readMessage 已读对象
     **/
    @PostMapping("/read")
    @LoginRequired
    public ResponseMessage readMessage(
            @RequestBody ReadMessage readMessage) {
        messageService.read(readMessage.getId(), readMessage.getUserId());
        return ResponseMessage.successMessage();
    }


    /**
     * @method getSendMessage 获取一个用户发送的站内信
     * @return Message 站内信
     **/
    @GetMapping("/user/send")
    @LoginRequired
    public ResponseMessage getSendMessage() {
        User user = UserHolder.get();
        if (user != null) {
            return ResponseMessage.successMessage(messageService.findByFromId(user.getId()));
        } else {
            return ResponseMessage.successMessage();
        }
    }
}