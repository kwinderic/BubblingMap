package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.ActivityTask;
import com.bubbling.pojo.ActivityUserLocation;
import com.bubbling.pojo.BubblingActInfo;
import com.bubbling.pojo.UserTaskState;
import com.bubbling.service.ActivityService;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.ReflectUtil;
import com.sun.media.sound.SoftTuning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.StandardEmitterMBean;
import javax.swing.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private ActivityService activityService;

    private CommonMessage commonMessage;


    @PostMapping("/createtask/{token}")
    public String addTask(@PathVariable("token") String token, ActivityTask activityTask) throws Exception {
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map;
        map = ReflectUtil.getObjectValue(activityTask);
        map.put("userPhone",userPhone);
        int state = activityService.createTask(map);
        commonMessage = new CommonMessage(200,"添加成功");
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/showactivities/{token}")
    public String showActivities(@PathVariable("token") String token) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        commonMessage = new CommonMessage(200,"展示成功",activityService.showActivities(map));
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/alterusertaskstate/{token}")
    public String alterUserTaskState(@PathVariable("token") String token , UserTaskState userTaskState) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map;
        map = ReflectUtil.getObjectValue(userTaskState);
        map.put("userPhone",userPhone);
        int state = activityService.alterUserTaskState(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"活动不正在进行");
        }else if(state == 3){
            commonMessage = new CommonMessage(313,"用户未参加活动");
        }else if(state == 4){
            commonMessage = new CommonMessage(314,"用户已经暂停/结束/退出");
        }else{
            commonMessage = new CommonMessage(200,"修改成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/activityrun/{token}/{time}")
    public String activityRun(@PathVariable("token") String token, @PathVariable("time")Date time) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("nowtime",time);
        commonMessage = new CommonMessage(200,"返回了应该启动的活动",activityService.activityRun(map));
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/activityfinish/{token}/{time}")
    public String activityFinish(@PathVariable("token") String token,@PathVariable("time")Date time) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map=new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("nowtime",time);
        commonMessage = new CommonMessage(200,"返回了应该结束的活动",activityService.activityFinish(map));
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/createactivity/{token}")
    public String createActivity(@PathVariable("token") String token,BubblingActInfo bubblingActInfo) throws Exception {
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map;
        map = ReflectUtil.getObjectValue(bubblingActInfo);
        System.out.println(map);
        activityService.createActivity(map);
        commonMessage = new CommonMessage(200,"活动发起成功");
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/showacttask/{token}/{activityid}")
    public String showActTask(@PathVariable("token") String token,@PathVariable("activityid") String activityId) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        if(activityService.showActTask(map) == null){
            commonMessage = new CommonMessage(311,"活动不存在或者没有任务点",null);
        }else{
            commonMessage = new CommonMessage(200,"返回活动的任务点",activityService.showActTask(map));
        }
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/showusertaskstate/{token}/{activityid}")
    public String showUserTaskState(@PathVariable("token") String token,@PathVariable("activityid") String activityId ) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        if(activityService.showUserTaskState(map) == null){
            commonMessage = new CommonMessage(311,"活动不存在或者用户未参加活动或者不存在任务点",null);
        }else{
            System.out.println(map);
            commonMessage = new CommonMessage(200,"返回用户任务点状态",activityService.showUserTaskState(map));
        }
        return JSON.toJSONString(commonMessage);
    }
}