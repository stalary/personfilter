package com.stalary.personfilter.service.redis;

import com.stalary.personfilter.holder.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * RedisService
 *
 * @author lirongqian
 * @since 2018/04/19
 */
@Service
@Slf4j
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedis;

    public void setString(String key, String value) {
        stringRedis.opsForValue().set(key, value);
    }

    public String getString(String key) {
        return stringRedis.opsForValue().get(key);
    }

    public boolean remove(String key) {
        UserHolder.remove();
        return stringRedis.delete(key);
    }

}