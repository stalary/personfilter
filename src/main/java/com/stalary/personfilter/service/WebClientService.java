package com.stalary.personfilter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stalary.personfilter.data.dto.ProjectInfo;
import com.stalary.personfilter.data.dto.ResponseMessage;
import com.stalary.personfilter.holder.ProjectHolder;
import com.stalary.personfilter.utils.PFUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

/**
 * WebClient
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@Slf4j
@Service
public class WebClientService {

    @Resource
    private Gson gson;

    @Resource
    private StringRedisTemplate redis;
    /**
     * 用户中心的地址
     */
    @Value("${server.user}")
    private String userCenterServer;

    public WebClientService() {
        // 预热
        builder(userCenterServer, HttpMethod.GET, "");
    }

    public void getProjectInfo() {
        // 将项目信息存入缓存中
        if (StringUtils.isEmpty(redis.opsForValue().get(PFUtil.PROJECT))) {
            Mono<ResponseMessage> info = builder(userCenterServer, HttpMethod.GET, "/facade/project?name={name}&phone={phone}", "人才筛选", "17853149599");
            ResponseMessage block = info.block();
            redis.opsForValue().set(PFUtil.PROJECT, block.getData().toString());
            ProjectHolder.set(gson.fromJson(block.getData().toString(), ProjectInfo.class));
        }
    }


    private Mono<ResponseMessage> builder(String baseUrl, HttpMethod httpMethod, String uri, Object... uriVariables) {
        return WebClient.create(baseUrl)
                .method(httpMethod)
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.warn("error: {}, msg: {}", response.statusCode().value(), response.statusCode().getReasonPhrase());
                    return Mono.error(new RuntimeException(response.statusCode().value() + " : " + response.statusCode().getReasonPhrase()));
                })
                .bodyToMono(ResponseMessage.class)
                .retry(3)
                .doOnError(WebClientResponseException.class, err -> {
                    log.warn("error: {}, msg: {}", err.getRawStatusCode(), err.getResponseBodyAsString());
                })
                .doOnSuccess(responseMessage -> log.info("webClient: " + userCenterServer + uri + responseMessage));
    }

}