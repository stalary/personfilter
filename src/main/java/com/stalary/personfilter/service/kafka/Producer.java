
package com.stalary.personfilter.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Sender
 *
 * @author lirongqian
 * @since 2018/04/18
 */
@Slf4j
@Component
public class Producer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;


    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.info("send message: topic: " + topic + " message: " + message);
    }

    public void send(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
        log.info("send message: topic: " + topic + " key: " + key + " message: " + message);
    }
}