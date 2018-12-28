package com.stalary.personfilter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * 用户中心调用
 * @author Stalary
 * @description
 * @date 2018/12/28
 */
@FeignClient(name = "user", url = "${server.user}")
@Component
public interface UserCenterClient {
}
