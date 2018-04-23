package com.stalary.personfilter.service.outer;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.stalary.personfilter.data.ResultEnum;
import com.stalary.personfilter.data.dto.SendResume;
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.exception.MyException;
import com.stalary.personfilter.factory.BeansFactory;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.utils.Constant;
import com.stalary.personfilter.utils.PFUtil;
import io.goeasy.GoEasy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * CommonService
 * 公共服务类
 *
 * @author lirongqian
 * @since 2018/04/16
 */
@Service
@Slf4j
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