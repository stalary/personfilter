package com.stalary.personfilter.service;

import com.google.gson.Gson;
import com.stalary.personfilter.data.dto.*;
import com.stalary.personfilter.holder.ProjectHolder;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * WebClient
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@Slf4j
@Service
public class WebClientService {

    @Autowired
    private Gson gson;

    @Autowired
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
                .doOnError(WebClientResponseException.class, err -> log.warn("error: {}, msg: {}", err.getRawStatusCode(), err.getResponseBodyAsString()))
                .doOnSuccess(responseMessage -> log.info("webClient: " + userCenterServer + uri + responseMessage));
    }

    public void getProjectInfo() {
        // 将项目信息存入缓存中
        if (StringUtils.isEmpty(redis.opsForValue().get(Constant.PROJECT))) {
            Mono<ResponseMessage> info = builder(userCenterServer, HttpMethod.GET, "/facade/project?name={name}&phone={phone}", "leader直聘", "17853149599");
            ResponseMessage block = info.block();
            if (block != null) {
                redis.opsForValue().set(Constant.PROJECT, block.getData().toString());
                ProjectHolder.set(gson.fromJson(block.getData().toString(), ProjectInfo.class));
            }
        } else {
            // 存在缓存时直接取出
            ProjectHolder.set(gson.fromJson(redis.opsForValue().get(Constant.PROJECT), ProjectInfo.class));
        }
    }

    public ResponseMessage postUser(Object object, String type) {
        ProjectInfo projectInfo = ProjectHolder.get();
        User user = new User();
        if (object instanceof Applicant) {
            Applicant applicant = (Applicant) object;
            user.setUsername(applicant.getUsername())
                    .setPassword(applicant.getPassword())
                    .setPhone(applicant.getPhone())
                    .setEmail(applicant.getEmail())
                    .setProjectId(projectInfo.getProjectId())
                    .setRole(2);
        } else if (object instanceof HR) {
            HR hr = (HR) object;
            user.setUsername(hr.getUsername())
                    .setNickname(hr.getNickname())
                    .setPassword(hr.getPassword())
                    .setPhone(hr.getPhone())
                    .setEmail(hr.getEmail())
                    .setProjectId(projectInfo.getProjectId())
                    .setFirstId(hr.getCompanyId())
                    .setRole(1);
        } else {
            user = (User) object;
        }
        user.setProjectId(projectInfo.getProjectId());
        ResponseMessage tokenResponse = WebClient
                .create(userCenterServer)
                .method(HttpMethod.POST)
                .uri("/token/{register}?key={key}", type, projectInfo.getKey())
                .body(Mono.just(user), User.class)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.warn("error: {}, msg: {}", response.statusCode().value(), response.statusCode().getReasonPhrase());
                    return Mono.error(new RuntimeException(response.statusCode().value() + " : " + response.statusCode().getReasonPhrase()));
                })
                .bodyToMono(ResponseMessage.class)
                .block();
        if (tokenResponse != null) {
            getUser(tokenResponse.getData().toString());
        }
        return tokenResponse;
    }

    /**
     * 获取到用户信息时即存入缓存
     * @param token
     * @return
     */
    public ResponseMessage getUser(String token) {
        ResponseMessage userResponse = null;
        if (StringUtils.isEmpty(redis.opsForValue().get(Constant.USER))) {
            ProjectInfo projectInfo = ProjectHolder.get();
            Mono<ResponseMessage> builder = builder(userCenterServer, HttpMethod.GET, "/facade/token?token={token}&key={key}", token, projectInfo.getKey());
            userResponse = builder.block();
            if (userResponse != null) {
                redis.opsForValue().set(Constant.TOKEN + Constant.SPLIT + token, userResponse.getData().toString());
                log.info("user: " + userResponse.getData().toString());
                UserHolder.set(gson.fromJson(userResponse.getData().toString(), User.class));
            }
        } else {
            UserHolder.set(gson.fromJson(redis.opsForValue().get(Constant.USER), User.class));
        }
        return userResponse;
    }

//    public User getUserById(Long userId) {
//        builder()
//    }

}