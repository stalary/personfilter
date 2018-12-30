
package com.stalary.personfilter.service.lightmq;

import com.alibaba.fastjson.JSONObject;
import com.stalary.lightmqclient.MQListener;
import com.stalary.lightmqclient.MessageDto;
import com.stalary.lightmqclient.facade.MQConsumer;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.Message;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.service.ClientService;
import com.stalary.personfilter.service.WebSocketService;
import com.stalary.personfilter.service.mysql.MessageService;
import com.stalary.personfilter.service.mysql.RecruitService;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.service.outer.MailService;
import com.stalary.personfilter.service.outer.MapdbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.stalary.personfilter.utils.Constant.*;

/**
 * Consumer
 *
 * @author lirongqian
 * @since 2018/04/18
 */
@Slf4j
@Component
public class Consumer implements MQConsumer {

    private static MapdbService mapdbService;

    @Autowired
    public void setMapdbService(MapdbService mapdbService) {
        Consumer.mapdbService = mapdbService;
    }

    private static MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        Consumer.messageService = messageService;
    }

    private static RecruitService recruitService;

    @Autowired
    public void setRecruitService(RecruitService recruitService) {
        Consumer.recruitService = recruitService;
    }

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        Consumer.userService = userService;
    }

    private static WebSocketService webSocketService;

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        Consumer.webSocketService = webSocketService;
    }

    private static MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        Consumer.mailService= mailService;
    }

    private static ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        Consumer.clientService = clientService;
    }

    @Override
    @MQListener(topics = {SEND_RESUME, "test"})
    public void process(MessageDto record) {
        long startTime = System.currentTimeMillis();
        String topic = record.getTopic();
        String key = "";
        if (record.getKey() != null) {
            key = record.getKey();
        }
        String message = record.getValue();
        log.info("receive message: topic: " + topic + " key: " + key + " message: " + message);
        if (SEND_RESUME.equals(topic)) {
            SendResume resume = JSONObject.parseObject(message, SendResume.class);
            if (HANDLE_RESUME.equals(key)) {
                // 处理投递简历
                mapdbService.handleResume(resume);
            } else if (SEND.equals(key)) {
                // 存储投递的消息通知(系统发送)
                Long userId = resume.getUserId();
                Message m = new Message(0L, userId, "简历投递成功", resume.getTitle() + "简历投递成功", false);
                messageService.save(m);
                // 统计通知未读的数量
                int count = messageService.findNotRead(userId).size();
                webSocketService.sendMessage(userId, "" + count);
            } else if (RECEIVE.equals(key)) {
                // 存储收到简历的消息通知(系统发送)
                Long recruitId = resume.getRecruitId();
                Long userId = resume.getUserId();
                Recruit recruit = recruitService.findOne(recruitId);
                UserInfo userInfo = userService.findOne(userId);
                Long hrId = recruit.getHrId();
                User hr = clientService.getUser(hrId);
                Message m = new Message(0L, hrId, resume.getTitle() + "收到简历", resume.getTitle() + "收到来自" + userInfo.getSchool() + "的" + userInfo.getNickname() + "的简历", false);
                messageService.save(m);
                mailService.sendResume(hr.getEmail(), resume.getTitle() + "收到来自" + userInfo.getSchool() + "的" + userInfo.getNickname() + "的简历");
                // 统计通知未读的数量
                int count = messageService.findNotRead(hrId).size();
                webSocketService.sendMessage(hrId, "" + count);
            }
        } else {
            log.info("receive message:" + record);
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
