
package com.stalary.personfilter.service.kafka;

import com.google.gson.Gson;
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

    @KafkaListener(topics = {NOTIFY})
    public void process(ConsumerRecord record) {
        long startTime = System.currentTimeMillis();
        String topic = record.topic();
        String key = "";
        if (record.key() != null) {
            key = record.key().toString();
        }
        String message = record.value().toString();
        if (NOTIFY.equals(topic)) {
            if (SEND.equals(key)) {

            } else if (RECEIVE.equals(key)) {

            } else if (LOOK.equals(key)) {

            }
            log.info("kafka: " + topic + key + message);
        } else if (SEND_RESUME.equals(topic)) {

        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
