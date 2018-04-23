
package com.stalary.personfilter.service.kafka;

import com.google.gson.Gson;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.entity.mysql.Message;
import com.stalary.personfilter.factory.BeansFactory;
import com.stalary.personfilter.service.mysql.MessageService;
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
                mapdbService.handleResume(resume);
            } else if (SEND.equals(key)) {
//                Message m = new Message(0L, resume.getUserId(), "简历投递成功", "");
//                messageService.save()
            } else if (RECEIVE.equals(key)) {

            }
        } else if (NOTIFY.equals(topic)) {
            log.info("notify");
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
