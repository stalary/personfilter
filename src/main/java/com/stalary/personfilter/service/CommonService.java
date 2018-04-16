package com.stalary.personfilter.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.stalary.personfilter.data.ResultEnum;
import com.stalary.personfilter.exception.MyException;
import com.stalary.personfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

/**
 * CommonService
 * 公共服务类
 * @author lirongqian
 * @since 2018/04/16
 */
@Service
@Slf4j
public class CommonService {

    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI7Si7prUFjTnd";
    private static final String ACCESS_KEY_SECRET = "HpNn37b9MRj02dyDIJMnSikmomOGAx";

    private IAcsClient getAcsClient() throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        return new DefaultAcsClient(profile);
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
}