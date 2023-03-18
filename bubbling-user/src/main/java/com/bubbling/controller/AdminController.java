package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.service.AdminService;
import com.bubbling.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(("/admin"))
public class AdminController {
    @Autowired
    private AdminService adminService;
    private CommonMessage commonMessage;

    @PostMapping("/addactivity/{activityNo}/{token}")
    public String addActivity(@PathVariable("token") String token, @PathVariable("activityNo") String activityNo){
        try {
            String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            adminService.adminAddActivity(userPhone, activityNo);
            commonMessage = new CommonMessage(210, "添加成功");
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/deleteactivity/{activityNo}/{token}")
    public String deleteActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        try {
            String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            adminService.adminDeleteActivity(userPhone, activityNo);
            commonMessage = new CommonMessage(210, "删除成功");
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/getactivitylist/{token}")
    public String getActivityList(@PathVariable("token") String token){
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            List<Map<String, String>> list = adminService.getAdminActivityList(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", list);
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }
}
