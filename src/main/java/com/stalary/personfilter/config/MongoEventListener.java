package com.stalary.personfilter.config;

import com.stalary.personfilter.annotation.AutoValue;
import com.stalary.personfilter.annotation.CreateTime;
import com.stalary.personfilter.annotation.UpdateTime;
import com.stalary.personfilter.data.entity.SeqInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.annotation.Retention;
import java.time.LocalDateTime;

/**
 * MongoEventListener
 *
 * @author lirongqian
 * @since 2018/04/13
 */
@Component
@Slf4j
public class MongoEventListener extends AbstractMongoEventListener<Object> {

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongo;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            // 如果字段添加了我们自定义的AutoValue注解
            if (field.isAnnotationPresent(AutoValue.class) && field.get(source) instanceof Number
                    && field.getLong(source) == 0) {
                // 判断注解的字段是否为number类型且值是否等于0.如果大于0说明有ID不需要生成ID
                // 设置自增ID
                field.set(source, getNextId(source.getClass().getSimpleName()));
                log.debug("集合的ID为=======================" + source);
            }
            if (field.isAnnotationPresent(CreateTime.class) && field.get(source) == null) {
                field.set(source, LocalDateTime.now());
            }
            if (field.isAnnotationPresent(UpdateTime.class)) {
                field.set(source, LocalDateTime.now());

            }
        });
    }

    private Long getNextId(String collName) {
        log.debug("CollectionsName======================="+collName);
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqInfo seq = mongo.findAndModify(query, update, options, SeqInfo.class);
        log.debug(collName+" 集合的ID为======================="+seq.getSeqId());
        return seq.getSeqId();
    }
}