package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.ActivityTask;
import com.bubbling.pojo.ActivityUserLocation;
import com.bubbling.pojo.BubblingActInfo;
import com.bubbling.service.ActivityService;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.StandardEmitterMBean;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("activity")
public class ActivityController {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private ActivityService activityService;

    private CommonMessage commonMessage;

    @PostMapping("/recorduserlocation/{token}")
    public String recordUserLocation(@PathVariable("token") String token, ActivityUserLocation activityUserLocation){
        try {
            JWTUtil.verify(token).getClaim("userPhone");

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


    @PostMapping("/createtask/{token}")
    public String addTask(@PathVariable("token") String token, ActivityTask activityTask) throws Exception {
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map;
        map = ReflectUtil.getObjectValue(activityTask);
        map.put("userPhone",userPhone);
        int state = activityService.createTask(map);
        commonMessage = new CommonMessage(210,"添加成功");
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/activity/showactivities/{token}")
    public String showActivities(@PathVariable("token") String token, BubblingActInfo bubblingActInfo) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map;
        map = ReflectUtil.getObjectValue(bubblingActInfo);
        map.put("userPhone",userPhone);
        commonMessage = new CommonMessage(210,"展示成功",activityService.showActivities(map));
        return JSON.toJSONString(commonMessage);
    }




}
