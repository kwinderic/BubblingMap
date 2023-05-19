package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.bubbling.dto.CommonMessage;
import com.bubbling.utils.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(("/developer"))
public class DeveloperController {
    private CommonMessage commonMessage;

    /**
     * 开发者控制短信验证码功能是否开启
     * 【已测试】
     * 2022-03-23 17:15:51 GMT+8
     * @author k
     */
    @RequestMapping("/change/{password}")
    public String changeEnableVerificationState(@PathVariable("password") String password){
        if("bubbling".equals(password)){
            ConstantUtil.enableVerification=!ConstantUtil.enableVerification;
            commonMessage = new CommonMessage(200, "修改成功","当前状态为: "+ConstantUtil.enableVerification);
        }else commonMessage = new CommonMessage(201, "密钥错误，更改失败","当前状态为: "+ConstantUtil.enableVerification);
        return JSON.toJSONString(commonMessage);
    }
}
