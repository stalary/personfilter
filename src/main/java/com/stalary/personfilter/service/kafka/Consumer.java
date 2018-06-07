
package com.stalary.personfilter.service.kafka;

import com.google.gson.Gson;
import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.Message;
import com.stalary.personfilter.data.entity.mysql.Recruit;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.factory.BeansFactory;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.service.WebSocketService;
import com.stalary.personfilter.service.mysql.MessageService;
import com.stalary.personfilter.service.mysql.RecruitService;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.service.outer.GoEasyService;
import com.stalary.personfilter.service.outer.MailService;
import com.stalary.personfilter.service.outer.MapdbService;
import com.stalary.personfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
public class Consumer {

    @Autowired
    private Gson gson;

    @Autowired
    private MapdbService mapdbService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MailService mailService;

    @Autowired
    private WebClientService webClientService;

    @KafkaListener(topics = {SEND_RESUME, NOTIFY})
    public void process(ConsumerRecord record) {
        long startTime = System.currentTimeMillis();
        String topic = record.topic();
        String key = "";
        if (record.key() != null) {
            key = record.key().toString();
        }
        String message = record.value().toString();
        log.info("receive message: topic: " + topic + " key: " + key + " message: " + message);
        if (SEND_RESUME.equals(topic)) {
            SendResume resume = gson.fromJson(message, SendResume.class);
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
                User hr = webClientService.getUser(hrId);
                Message m = new Message(0L, hrId, resume.getTitle() + "收到简历", resume.getTitle() + "收到来自" + userInfo.getSchool() + "的" + userInfo.getNickname() + "的简历", false);
                messageService.save(m);
                mailService.sendResume(hr.getEmail(), resume.getTitle() + "收到来自" + userInfo.getSchool() + "的" + userInfo.getNickname() + "的简历");
                // 统计通知未读的数量
                int count = messageService.findNotRead(hrId).size();
                webSocketService.sendMessage(hrId, "" + count);
            }
        } else if (NOTIFY.equals(topic)) {
            log.info("notify");
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
