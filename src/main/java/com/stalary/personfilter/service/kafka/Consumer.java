
package com.stalary.personfilter.service.kafka;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer
 *
 * @author lirongqian
 * @since 2018/04/18
 */
@Slf4j
@Component
public class Consumer {

    public static final String NOTIFY = "notify";

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
            log.info("kafka: " + topic + key + message);
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
