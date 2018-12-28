package com.stalary.personfilter.service;

import com.alibaba.fastjson.JSONObject;
import com.stalary.personfilter.data.dto.Applicant;
import com.stalary.personfilter.data.dto.HR;
import com.stalary.personfilter.data.dto.ProjectInfo;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.vo.ResponseMessage;
import com.stalary.personfilter.exception.MyException;
import com.stalary.personfilter.holder.ProjectHolder;
import com.stalary.personfilter.holder.TokenHolder;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.redis.RedisKeys;
import com.stalary.personfilter.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.stalary.personfilter.utils.Constant.*;

/**
 * WebClient
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@Slf4j
@Service
public class ClientService {

    @Resource
    private RedisService redisService;
    /**
     * 用户中心的地址
     */
    @Value("${server.user}")
    private String userCenterServer;

    public ClientService() {
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
                ProjectHolder.set(JSONObject.parseObject(JSONObject.toJSONString(block.getData()), ProjectInfo.class));
            }
        } else {
            // 存在缓存时直接取出
            ProjectHolder.set(JSONObject.parseObject(redisService.getString(RedisKeys.PROJECT_INFO), ProjectInfo.class));
        }
    }

    public ResponseMessage postUser(Object object, String type) {
        ProjectInfo projectInfo = ProjectHolder.get();
        User user;
        // 当登录注册时，判断对象类型
        if (object instanceof Applicant) {
            Applicant applicant = (Applicant) object;
            user = new User().toBuilder().username(applicant.getUsername())
                    .password(applicant.getPassword())
                    .phone(applicant.getPhone())
                    .email(applicant.getEmail())
                    .projectId(projectInfo.getProjectId())
                    .role(2).build();
        } else if (object instanceof HR) {
            HR hr = (HR) object;
            user = new User().toBuilder().username(hr.getUsername())
                    .nickname(hr.getNickname())
                    .password(hr.getPassword())
                    .phone(hr.getPhone())
                    .email(hr.getEmail())
                    .projectId(projectInfo.getProjectId())
                    .firstId(hr.getCompanyId())
                    .role(1).build();
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
                // 修改成功后清空缓存
                redisService.remove(getKey(RedisKeys.USER_TOKEN, TokenHolder.get()));
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
                User user = JSONObject.parseObject(userResponse.getData().toString(), User.class);
                UserHolder.set(user);
                return user;
            } else {
                throw new MyException(userResponse.getCode(), userResponse.getMsg());
            }
        } else {
            User user = JSONObject.parseObject(redisService.getString(getKey(RedisKeys.USER_TOKEN, token)), User.class);
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
        if (projectInfo == null) {
            getProjectInfo();
        }
        projectInfo = ProjectHolder.get();
        Mono<ResponseMessage> builder = builder(userCenterServer, HttpMethod.GET, "/facade/user?userId={userId}&key={key}&projectId={projectId}", userId, projectInfo.getKey(), projectInfo.getProjectId());
        ResponseMessage block = builder.block();
        if (block.isSuccess()) {
            return JSONObject.parseObject(block.getData().toString(), User.class);
        }
        return null;
    }

}