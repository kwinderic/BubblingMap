package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.bubbling.dto.CommonMessage;
import com.bubbling.service.SuperintendentService;
import com.bubbling.service.UserService;
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
 * 管理创建的活动
 * @author k
 */
@Slf4j
@RestController
@RequestMapping(("/superintendent"))
public class SuperintendentController {
    @Autowired
    private SuperintendentService superintendentService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    private CommonMessage commonMessage;

    /**
     * 发布一个活动
     * 【已测试】
     * 2022-03-26 10:04:25 GMT+8
     * @author k
     */
    @PostMapping("/addactivity/{token}/{activityId}/{longitude}/{latitude}")
    public String addActivity(@PathVariable("token") String token, @PathVariable("activityId") String activityId,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(superintendentService.superintendentCreateActivity(userPhone, activityId)==1){
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
    @PostMapping("/deleteactivity/{token}/{activityId}")
    public String deleteActivity(@PathVariable("token") String token,@PathVariable("activityId") String activityId){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        redisUtil.geoRemove(ConstantUtil.onlineActivity,activityId);
        if(superintendentService.superintendentDeleteActivity(userPhone, activityId)==1)
            commonMessage = new CommonMessage(210, "删除成功");
        else commonMessage = new CommonMessage(211, "删除失败");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 获取发布的活动列表
     * 【已测试】
     * 2022-03-20 10:41:57 GMT+8
     * @author k
     */
    @GetMapping("/getcreateactivitylist/{token}")
    public String getCreateActivityList(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        List<Map<String, String>> list = superintendentService.getSuperintendentActivityList(userPhone);
        commonMessage = new CommonMessage(210, "获取成功", list);
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 获取申请参加活动的列表
     * 2022-03-26 15:14:45 GMT+8
     * @author k
     */
    @GetMapping("/getapplylist/{token}/{activityId}")
    public String getApplyList(@PathVariable("token") String token,@PathVariable("activityId") String activityId){
        List<Map<String, String>> list = superintendentService.getUserApplyList(activityId);
        commonMessage = new CommonMessage(210, "获取成功", list);
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 同意申请
     * 2022-03-26 15:19:27 GMT+8
     * @author k
     */
    @PostMapping("/passapply/{token}/{activityId}/{userPhone}")
    public String passApply(@PathVariable("userPhone") String userPhone,@PathVariable("activityId") String activityId){
        superintendentService.superintendentPassApply(userPhone, activityId);
        commonMessage = new CommonMessage(210, "已通过");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 拒绝申请
     * 2022-03-26 15:19:55 GMT+8
     * @author k
     */
    @PostMapping("/rejectapply/{token}/{activityId}/{userPhone}")
    public String rejectApply(@PathVariable("userPhone") String userPhone,@PathVariable("activityId") String activityId){
        superintendentService.superintendentRejectApply(userPhone, activityId);
        commonMessage = new CommonMessage(210, "已拒绝");
        return JSON.toJSONString(commonMessage);
    }
}
