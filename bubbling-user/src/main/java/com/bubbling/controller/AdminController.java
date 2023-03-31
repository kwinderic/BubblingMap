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

    @PostMapping("/addactivity/{token}/{activityId}/{longitude}/{latitude}")
    public String addActivity(@PathVariable("token") String token, @PathVariable("activityId") String activityId,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(adminService.adminAddActivity(userPhone, activityId)==1){
            redisUtil.geoAdd(ConstantUtil.onlineActivity, new Point(Double.parseDouble(longitude),Double.parseDouble(latitude)), activityId);
            commonMessage = new CommonMessage(210, "添加成功");
        }
        else commonMessage = new CommonMessage(211, "添加失败");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 删除创建的活动
     * 【已测试】
     * 2022-03-20 10:27:20 GMT+8
     * @author k
     */
    @DeleteMapping("/deleteactivity/{token}/{activityId}")
    public String deleteActivity(@PathVariable("token") String token,@PathVariable("activityId") String activityId){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        redisUtil.geoRemove(ConstantUtil.onlineActivity,activityId);
        if(adminService.adminDeleteActivity(userPhone, activityId)==1)
            commonMessage = new CommonMessage(210, "删除成功");
        else commonMessage = new CommonMessage(211, "删除失败");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 获取创建的活动列表
     * 【已测试】
     * 2022-03-20 10:41:57 GMT+8
     * @author k
     */
    @GetMapping("/getactivitylist/{token}")
    public String getActivityList(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        List<Map<String, String>> list = adminService.getAdminActivityList(userPhone);
        commonMessage = new CommonMessage(210, "获取成功", list);
        return JSON.toJSONString(commonMessage);
    }
}
