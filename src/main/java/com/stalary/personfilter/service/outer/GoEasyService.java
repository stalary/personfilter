package com.stalary.personfilter.service.outer;

import io.goeasy.GoEasy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CommonService
 * 公共服务类
 *
 * @author lirongqian
 * @since 2018/04/16
 */
@Service
@Slf4j
@Deprecated
public class GoEasyService {

    /**
     * goeasy推送
     */
    private static final String REGION_HOST = "https://rest-hangzhou.goeasy.io";

    private static final String COMMON_KEY = "BC-9e32a2089e08457399dfc6032fcaa294";

    /**
     * 构造goeasy单例
     * @return
     */
    private static GoEasy getInstance() {
        return GoEasyHandler.instance;
    }

    private static class GoEasyHandler {
        private static GoEasy instance = new GoEasy(REGION_HOST, COMMON_KEY);
    }

    public void pushMessage(String channel, String message) {
        GoEasy goEasy = getInstance();
        goEasy.publish(channel, message);
    }

}