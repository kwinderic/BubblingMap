package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.service.AdminService;
import com.bubbling.utils.ConstantUtil;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 2022-03-12 11:23:35 GMT+8
 * 获取用户创建的活动
 * @author k
 */
@Slf4j
@RestController
@RequestMapping(("/admin"))
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisUtil redisUtil;
    private CommonMessage commonMessage;

    @PostMapping("/addactivity/{token}/{activityNo}/{longitude}/{latitude}")
    public String addActivity(@PathVariable("token") String token, @PathVariable("activityNo") String activityNo,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(adminService.adminAddActivity(userPhone, activityNo)==1){
            redisUtil.geoAdd(ConstantUtil.onlineActivity, new Point(Double.parseDouble(longitude),Double.parseDouble(latitude)), activityNo);
            commonMessage = new CommonMessage(210, "添加成功");
        }
        else commonMessage = new CommonMessage(211, "添加失败");
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/deleteactivity/{token}/{activityNo}")
    public String deleteActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        redisUtil.geoRemove(ConstantUtil.onlineActivity,activityNo);
        if(adminService.adminDeleteActivity(userPhone, activityNo)==1)
            commonMessage = new CommonMessage(210, "删除成功");
        else commonMessage = new CommonMessage(211, "添加失败");
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/getactivitylist/{token}")
    public String getActivityList(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        List<Map<String, String>> list = adminService.getAdminActivityList(userPhone);
        commonMessage = new CommonMessage(210, "获取成功", list);
        return JSON.toJSONString(commonMessage);
    }
}
