package com.bubbling.utils;

import com.alibaba.fastjson.JSON;
import com.bubbling.mapper.UserMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SendSMSUtil {
    @Autowired
    private UserMapper userMapper;

    public String sendVerificationCode(String secretId, String secretKey, String phoneNumber, String templateParam) throws TencentCloudSDKException{
        Credential cred = new Credential(secretId, secretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);

        httpProfile.setEndpoint("sms.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();

        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, "ap-beijing",clientProfile);
        SendSmsRequest req = new SendSmsRequest();

        String sdkAppId = "1400639552";
        req.setSmsSdkAppId(sdkAppId);

        String signName = "吹泡泡个人应用";
        req.setSignName(signName);

        String templateId = "1323306";
        req.setTemplateId(templateId);

        String[] templateParamSet = {templateParam};
        req.setTemplateParamSet(templateParamSet);

        String[] phoneNumberSet = {"+86"+phoneNumber};
        log.info("[phoneNumber]: "+phoneNumber);
        req.setPhoneNumberSet(phoneNumberSet);

        SendSmsResponse res = client.SendSms(req);

        return SendSmsResponse.toJsonString(res);
    }

    public Integer recordSendSMS(String result, String verificationCode){
        String json1=result.substring(18, result.indexOf('}')+1);
        String json2="{"+result.substring(result.length()-51);
        Map<String, Object> map = JSON.parseObject(json1), map2 = JSON.parseObject(json2);
        map.put("RequestId",map2.get("RequestId"));
        map.put("VerificationCode", verificationCode);
        return userMapper.insertSendVerificationCodeRecord(map);
    }
}
