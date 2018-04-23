package com.stalary.personfilter.service;

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
import com.stalary.personfilter.data.dto.User;
import com.stalary.personfilter.data.entity.mysql.UserInfo;
import com.stalary.personfilter.exception.MyException;
import com.stalary.personfilter.holder.UserHolder;
import com.stalary.personfilter.service.mysql.UserService;
import com.stalary.personfilter.utils.Constant;
import com.stalary.personfilter.utils.PFUtil;
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
public class CommonService {

    /**
     * 阿里短信
     **/
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI7Si7prUFjTnd";
    private static final String ACCESS_KEY_SECRET = "HpNn37b9MRj02dyDIJMnSikmomOGAx";
    /**
     * 七牛云
     **/
    private static String ACCESS_KEY = "zfg7aGCs98DbKp_zKHzOAwzd6BoPhLjPkO5ohzEG";
    private static String SECRET_KEY = "l-fs00VcPP2nZRBKZmJj7LeSShi2wKxSMN5RL10w";
    private static String BUCKET_NAME = "stalary";
    private static String QINIU_IMAGE_DOMAIN = "http://p037i675p.bkt.clouddn.com/";
    private Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    Configuration cfg = new Configuration(Zone.huadong());
    private UploadManager uploadManager = new UploadManager(cfg);

    @Autowired
    private UserService userService;

    private IAcsClient getAcsClient() throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        return new DefaultAcsClient(profile);
    }

    private String getUpToken() {
        return auth.uploadToken(BUCKET_NAME);
    }

    public String sendCode(String phone) {
        String randomCode = RandomStringUtils.randomNumeric(6);
        if (sendSms(phone, randomCode)) {
            return randomCode;
        }
        return null;
    }

    private boolean sendSms(String phoneNumber, String randomCode) {
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(phoneNumber);
        request.setSignName("leader直聘");
        request.setTemplateCode("SMS_130924380");
        request.setTemplateParam("{\"code\":\"" + randomCode + "\"}");
        try {
            SendSmsResponse sendSmsResponse = getAcsClient().getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals(Constant.OK)) {
                log.info("发送短信成功");
                return true;
            }
            log.error(sendSmsResponse.getCode());
        } catch (ClientException e) {
            log.error("ClientException异常：" + e.getMessage());
            throw new MyException(ResultEnum.SEND_NOTE_ERROR);
        }
        log.error("发送短信失败");
        return false;
    }

    public String uploadPicture(MultipartFile picture) {
        String name = PFUtil.getPicture();
        try {
            Response response = uploadManager.put(picture.getBytes(), name, getUpToken());
            if (response.isOK() && response.isJson()) {
                UserInfo info = userService.getInfo();
                info.setAvatar(QINIU_IMAGE_DOMAIN + name);
                userService.save(info);
            }
        } catch (Exception e) {
            log.warn("upload picture error", e);
            throw new MyException(ResultEnum.QINIU_ERROR);
        }
        return QINIU_IMAGE_DOMAIN + name;
    }


}