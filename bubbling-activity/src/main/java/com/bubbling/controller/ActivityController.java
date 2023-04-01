package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.ActivityTask;
import com.bubbling.pojo.BubblingActInfo;
import com.bubbling.pojo.UserTaskState;
import com.bubbling.service.ActivityService;
import com.bubbling.service.UserServiceConsumer;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private UserServiceConsumer userServiceConsumer;


    /**
     * 展示所有的活动 【已测试】
     * @param token
     * @return BubblingActInfo
     * @author lzh
     */
    @GetMapping("/showactivities/{token}")
    public String showActivities(@PathVariable("token") String token) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        commonMessage = new CommonMessage(200,"展示成功",activityService.showActivities(map));
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 修改用户任务完成状态 【已测试】
     * @param token
     * @param userTaskState
     * @date 2022-03-20 18:50:36
     * @author lzh
     */
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

    /**
     *发起活动  【已测试】
     * @param token
     * @param
     * @date 2022-03-20 22:04:07
     * @author lzh
     */
    @PostMapping("/createactivity/{token}")
    public String createActivity(@PathVariable("token") String token,@RequestBody BubblingActInfo bubblingActInfo) throws Exception {
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map;
        map = ReflectUtil.getObjectValue(bubblingActInfo);
        List<Map<String,Object>> maps= new ArrayList<>();
        for (ActivityTask activityTask : bubblingActInfo.getTask()) {
            maps.add(ReflectUtil.getObjectValue(activityTask));
        }
        System.out.println(maps);
        activityService.createActivity(map,maps);
        userServiceConsumer.addActivity(token,map.get("activityId").toString(),map.get("longitude").toString(),map.get("latitude").toString());
        commonMessage = new CommonMessage(200,"活动发起成功");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 返回活动的所有任务点   【已测试】
     * @param token
     * @param activityId
     * @date 2022-03-20 18:42:31
     * @author lzh
     */
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

    /**
     * 返回用户任务完成状态   【已测试】
     * @param token
     * @param activityId
     * @date 2022-03-20 18:44:17
     * @author lzh
     */
    @GetMapping("/showusertaskstate/{token}/{activityid}")
    public String showUserTaskState(@PathVariable("token") String token,@PathVariable("activityid") String activityId ) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        if(activityService.showUserTaskState(map) == null){
            commonMessage = new CommonMessage(311,"活动不存在或者用户未参加活动或者不存在任务点",null);
        }else{
            commonMessage = new CommonMessage(200,"返回用户任务点状态",activityService.showUserTaskState(map));
        }
        return JSON.toJSONString(commonMessage);
    }
}