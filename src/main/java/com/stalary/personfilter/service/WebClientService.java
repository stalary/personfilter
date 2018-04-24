package com.stalary.personfilter.service;

import com.google.gson.Gson;
import com.stalary.personfilter.data.dto.*;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.exception.MyException;
import com.stalary.personfilter.holder.ProjectHolder;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.redis.RedisKeys;
import com.stalary.personfilter.service.redis.RedisService;
import static com.stalary.personfilter.utils.Constant.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
    private RedisService redisService;
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
        if (StringUtils.isEmpty(redisService.getString(RedisKeys.PROJECT_INFO))) {
            Mono<ResponseMessage> info = builder(userCenterServer, HttpMethod.GET, "/facade/project?name={name}&phone={phone}", "leader直聘", "17853149599");
            ResponseMessage block = info.block();
            if (block.isSuccess()) {
                redisService.setString(RedisKeys.PROJECT_INFO, block.getData().toString());
                ProjectHolder.set(gson.fromJson(block.getData().toString(), ProjectInfo.class));
            }
        } else {
            // 存在缓存时直接取出
            ProjectHolder.set(gson.fromJson(redisService.getString(RedisKeys.PROJECT_INFO), ProjectInfo.class));
        }
    }

    public ResponseMessage postUser(Object object, String type) {
        ProjectInfo projectInfo = ProjectHolder.get();
        User user = new User();
        // 当登录注册时，判断对象类型
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
        // 修改密码时需要特别判断
        String password = "";
        if (UPDATE_PASSWORD.equals(type)) {
            type = UPDATE;
            password = PASSWORD;
        }
        // 存入项目的id
        user.setProjectId(projectInfo.getProjectId());
        ResponseMessage tokenResponse = WebClient
                .create(userCenterServer)
                .method(HttpMethod.POST)
                .uri("/token/{type}/{password}?key={key}", type, password, projectInfo.getKey())
                .body(Mono.just(user), User.class)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.warn("error: {}, msg: {}", response.statusCode().value(), response.statusCode().getReasonPhrase());
                    return Mono.error(new RuntimeException(response.statusCode().value() + " : " + response.statusCode().getReasonPhrase()));
                })
                .bodyToMono(ResponseMessage.class)
                .block();
        if (UPDATE.equals(type)) {
            if (tokenResponse.isSuccess()) {
                return ResponseMessage.successMessage("修改成功");
            } else {
                return tokenResponse;
            }
        }
        // 当登陆注册时，需要用户token
        if (tokenResponse.isSuccess()) {
            getUser(tokenResponse.getData().toString());
        }
        return tokenResponse;
    }

    /**
     * 获取到用户信息时即存入缓存
     *
     * @param token
     * @return
     */
    public User getUser(String token) {
        if (StringUtils.isEmpty(redisService.getString(getKey(RedisKeys.USER_TOKEN, token)))) {
            ProjectInfo projectInfo = ProjectHolder.get();
            Mono<ResponseMessage> builder = builder(userCenterServer, HttpMethod.GET, "/facade/token?token={token}&key={key}", token, projectInfo.getKey());
            ResponseMessage userResponse = builder.block();
            if (userResponse.isSuccess()) {
                redisService.setString(getKey(RedisKeys.USER_TOKEN, token), userResponse.getData().toString());
                User user = gson.fromJson(userResponse.getData().toString(), User.class);
                UserHolder.set(user);
                return user;
            } else {
                throw new MyException(userResponse.getCode(), userResponse.getMsg());
            }
        } else {
            User user = gson.fromJson(redisService.getString(getKey(RedisKeys.USER_TOKEN, token)), User.class);
            UserHolder.set(user);
            return user;
        }
    }

    /**
     * 通过id获取用户信息
     *
     * @param userId
     * @return
     */
    public User getUser(Long userId) {
        ProjectInfo projectInfo = ProjectHolder.get();
        Mono<ResponseMessage> builder = builder(userCenterServer, HttpMethod.GET, "/facade/user?userId={userId}&key={key}&projectId={projectId}", userId, projectInfo.getKey(), projectInfo.getProjectId());
        ResponseMessage block = builder.block();
        if (block.isSuccess()) {
            return gson.fromJson(block.getData().toString(), User.class);
        }
        return null;
    }

}