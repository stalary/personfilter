package com.stalary.personfilter.interceptor;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.data.ResultEnum;
import com.stalary.personfilter.exception.MyException;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.service.redis.RedisKeys;
import com.stalary.personfilter.service.redis.RedisService;
import static com.stalary.personfilter.utils.Constant.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * LoginInterceptor
 *
 * @author lirongqian
 * @since 2018/04/09
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private WebClientService webClientService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        webClientService.getProjectInfo();
        Method method = ((HandlerMethod) handler).getMethod();
        String uri = request.getRequestURI();
        // 判断需要调用需要登陆的接口时是否已经登陆
        boolean isLoginRequired = isAnnotationPresent(method, LoginRequired.class);
        if (isLoginRequired) {
            String token = getToken(getAuthHeader(request));
            log.info("1" + webClientService.getUser(token));
            log.info("2" + token);
            if (webClientService.getUser(token) == null) {
                // token无法获取到用户信息代表未登陆
                throw new MyException(ResultEnum.NEED_LOGIN);
            }
            // 退出时删除缓存
            if (uri.contains(LOGOUT)) {
                redisService.remove(getKey(RedisKeys.USER_TOKEN, token));
            }
        }
        return true;
    }

    private boolean isAnnotationPresent(Method method, Class<? extends Annotation> annotationClass) {
        return method.getDeclaringClass().isAnnotationPresent(annotationClass) || method.isAnnotationPresent(annotationClass);
    }

    /**
     * 获取token
     */
    private String getAuthHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(Authorization);
        // 默认的auth
        log.info("authHeader" + authHeader);
        if (StringUtils.isEmpty(authHeader)) {
            authHeader = "Basic ea181087c67d85fcd58ee5b89808b4da6b4a859abb9d90e8b96c011418a10c2e";
        }
        return authHeader;
    }

    private String getToken(String authHeader) {
        String token = null;
        if (StringUtils.isNotEmpty(authHeader)) {
            token = authHeader.split(" ")[1];
        }
        return token;
    }
}