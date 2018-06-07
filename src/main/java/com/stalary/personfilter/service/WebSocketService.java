/**
 * @(#)WebSocketService.java, 2018-06-07.
 * <p>
 * Copyright 2018 Stalary.
 */
package com.stalary.personfilter.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.TimeUnit;

/**
 * WebSocketService
 *
 * @author lirongqian
 * @since 2018/06/07
 */
@Service
@ServerEndpoint("/message/{userId}")
@Slf4j
@Data
public class WebSocketService {

    private static Cache<Long, WebSocketService> sessionCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .concurrencyLevel(8)
            .maximumSize(1000)
            .recordStats()
            .build();

    private Long userId;

    private Session session;

    private String message;

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("userId") Long userId) {
        log.info("userId: " + userId + " webSocket开始连接");
        this.userId = userId;
        this.session = session;
        save(userId, this);
        WebSocketService present = sessionCache.getIfPresent(userId);
        if (present != null && StringUtils.isNotEmpty(present.getMessage())) {
            this.message = present.getMessage();
            sendMessage(userId, this.message);
        }
    }


    @OnClose
    public void onClose() {
        log.info("userId: " + userId + " webSocket关闭连接");
        sessionCache.invalidate(this.userId);
        log.info("sessionCache: " + sessionCache.asMap().keySet());
    }

    @OnError
    public void onError(Throwable e) {
        log.warn("webSocket error! ", e);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【webSocket】收到客户端发来的消息:{}", message);
    }

    /**
     * 向客户端发送消息
     **/
    @SneakyThrows
    public void sendMessage(Long userId, String message) {
        WebSocketService socket = sessionCache.getIfPresent(userId);
        // socket连接时直接发送消息
        if (socket != null && socket.getSession() != null) {
            socket.session
                    .getBasicRemote()
                    .sendText(message);
        } else {
            // 未连接时暂存消息
            WebSocketService webSocketService = new WebSocketService();
            webSocketService.setUserId(userId);
            webSocketService.setMessage(message);
            sessionCache.put(userId, webSocketService);
        }
    }

    public static void save(Long userId, WebSocketService webSocket) {
        WebSocketService present = sessionCache.getIfPresent(userId);
        // 当为null时，需要存储
        if (present == null) {
            sessionCache.put(userId, webSocket);
        } else {
            // 存在时表面已有缓存的消息，仅需要设置session
            present.setSession(webSocket.getSession());
        }
        log.info("sessionCache: " + sessionCache.asMap().keySet());
    }

    public static void remove(Long userId) {
        sessionCache.invalidate(userId);
    }

}