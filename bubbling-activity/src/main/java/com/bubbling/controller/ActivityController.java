package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.bubbling.dto.CommonMessage;
import com.bubbling.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

@RestController
@RequestMapping("activity")
public class ActivityController {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private ActivityService activityService;

    @PostMapping("/queryall")
    public String queryALlActivity(){
        CommonMessage commonMessage = new CommonMessage(400,"查询所有活动",activityService.queryAllActivity());
        return JSON.toJSONString(commonMessage);
    }


}
