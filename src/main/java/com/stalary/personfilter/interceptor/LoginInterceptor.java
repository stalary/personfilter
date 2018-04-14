package com.stalary.personfilter.interceptor;

import com.stalary.personfilter.annotation.LoginRequired;
import com.stalary.personfilter.service.WebClientService;
import com.stalary.personfilter.utils.Constant;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 当为swagger请求时跳过
//        if (request.getRequestURI().contains(Constant.SWAGGER)) {
//            return true;
//        }
        Method method = ((HandlerMethod) handler).getMethod();
        // 判断需要调用需要登陆的接口时是否已经登陆
        boolean isLoginRequired = isAnnotationPresent(method, LoginRequired.class);
        if (isLoginRequired) {
            // 获取项目信息，以便调用登陆中心
            webClientService.getProjectInfo();
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
        String authHeader = request.getHeader(Constant.Authorization);
        // 默认的auth
        if (StringUtils.isEmpty(authHeader)) {
            authHeader = "Basic 6d7535729f01dfb0ea1202a01b9f6328d36278ff9a79859297fbe8857edfdff8";
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