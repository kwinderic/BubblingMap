package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.ActivityTask;
import com.bubbling.pojo.ActivityUserLocation;
import com.bubbling.pojo.BubblingActInfo;
import com.bubbling.pojo.UserTaskState;
import com.bubbling.service.ActivityService;
import com.bubbling.service.UserServiceConsumer;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    public String alterUserTaskState(@PathVariable("token") String token ,@RequestBody UserTaskState userTaskState) throws Exception{
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
        map.replace("sponsorPhone",userPhone);
        activityService.createActivity(map,maps);
        userServiceConsumer.addActivity(token,map.get("activityId").toString(),map.get("longitude").toString(),map.get("latitude").toString());
        commonMessage = new CommonMessage(200,"活动发起成功");
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/dissolutionactivity/{token}/{activityId}")
    public String dissolutionActivity(@PathVariable("token") String token,@PathVariable("activityId") String activityId){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("userPhone",userPhone);
        int state = activityService.dissolutionActivity(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"不为创建者，无权限");
        }else{
            userServiceConsumer.deleteActivity(token,activityId);
            commonMessage = new CommonMessage(200,"活动解散成功");
        }
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

    /**
     * 记录用户坐标-时间 【已测试】
     * @param token
     * @param activityUserLocation
     * @date 2022-03-26 15:50:47
     * @author lzh
     */
    @PostMapping("/recorduserlocation/{token}")
    public String recordUserLocation(@PathVariable("token") String token,@RequestBody ActivityUserLocation activityUserLocation) throws Exception {
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = ReflectUtil.getObjectValue(activityUserLocation);
        map.put("userPhone",userPhone);
        int state = activityService.recordUserLocation(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"活动不正在进行");
        }else if(state == 3){
            commonMessage = new CommonMessage(313,"用户未参加活动");
        }else if(state == 4){
            commonMessage = new CommonMessage(314,"用户已经暂停/结束/退出");
        }else{
            commonMessage = new CommonMessage(200,"记录成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 展示用户自己参加的某一个活动的所有记录的坐标 【已测试】
     * @param token
     * @token 2022-03-26 15:59:03
     * @author lzh
     */
    @GetMapping("/showuserlocation/{token}/{activityId}")
    public String showUserLocation(@PathVariable("token") String token,@PathVariable("activityId") String activityId){
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        commonMessage = new CommonMessage(200,"返回了本次活动中自己的所有坐标",activityService.showUserLocation(map));
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 活动创建者查看该活动所有用户的所有坐标 【已测试】
     * @param token
     * @param activityId
     * @return
     */
    @GetMapping("/showalluserlocation/{token}/{activityId}")
    public String showAllUserLocation(@PathVariable("token") String token,@PathVariable("activityId") String activityId){
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        if(activityService.showAllUserLocation(map) == null ){
            commonMessage = new CommonMessage(311,"无权限，或者无数据");
        }else{
            commonMessage = new CommonMessage(200,"返回了本次活动中所有用户的坐标",activityService.showAllUserLocation(map));
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 修改活动信息 【已测试】
     * @param token
     * @param bubblingActInfo
     * @date 2022-03-26 20:03:04
     * @author lzh
     */
    @PostMapping("/alteractinfo/{token}")
    public String alterActInfo(@PathVariable("token") String token,@RequestBody BubblingActInfo bubblingActInfo) throws Exception {
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = ReflectUtil.getObjectValue(bubblingActInfo);
        map.put("userPhone",userPhone);
        int state = activityService.alterActInfo(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"活动已经结束");
        }else if(state == 3){
            commonMessage = new CommonMessage(313,"不为活动创建者，无权限");
        }else{
            commonMessage = new CommonMessage(200,"修改成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 添加任务 【已测试】
     * @param token
     * @param activityTask
     * @date 2022-03-26 20:05:19
     * @author lzh
     */
    @PostMapping("/addtask/{token}")
    public String addTask(@PathVariable("token") String token,@RequestBody ActivityTask activityTask) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = ReflectUtil.getObjectValue(activityTask);
        map.put("userPhone",userPhone);
        int state = activityService.addTask(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"活动已经结束");
        }else if(state == 3){
            commonMessage = new CommonMessage(313,"不为活动创建者，无权限");
        }else{
            commonMessage = new CommonMessage(200,"任务添加成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 删除任务 【已测试】
     * @param token
     * @param activityId
     * @param taskId
     * @date 2022-03-26 20:25:22
     * @author lzh
     */
    @PostMapping("/erasetask/{token}/{activityId}/{taskId}")
    public String eraseTask(@PathVariable("token") String token,@PathVariable("activityId") String activityId,@PathVariable("taskId") String taskId) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        map.put("taskId",taskId);
        int state = activityService.eraseTask(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"活动已经结束");
        }else if(state == 3){
            commonMessage = new CommonMessage(313,"不为活动创建者，无权限");
        }else{
            commonMessage = new CommonMessage(200,"任务删除成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 修改任务信息 【已测试】
     * @param token
     * @param activityTask
     * @date 2022-03-26 20:33:34
     * @author lzh
     */
    @PostMapping("/altertask/{token}")
    public String alterTask(@PathVariable("token") String token,@RequestBody ActivityTask activityTask) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = ReflectUtil.getObjectValue(activityTask);
        map.put("userPhone",userPhone);
        int state = activityService.alterTask(map);
        if(state == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(state == 2){
            commonMessage = new CommonMessage(312,"活动已经结束");
        }else if(state == 3){
            commonMessage = new CommonMessage(313,"不为活动创建者，无权限");
        }else{
            commonMessage = new CommonMessage(200,"任务修改成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 修改用户在活动中的状态 【已测试】
     * @param token
     * @param activityId
     * @param state
     * @date 2022-03-26 20:49:28
     * @author lzh
     */
    @PostMapping("/alteruseractprogress/{token}/{activityId}/{state}")
    public String alterUserActProgress(@PathVariable("token") String token,@PathVariable("activityId")String activityId,@PathVariable("state") int state) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        map.put("state",state);
        int sstate = activityService.alterUserActProgress(map);
        if(sstate == 1){
            commonMessage = new CommonMessage(311,"活动不存在");
        }else if(sstate == 2){
            commonMessage = new CommonMessage(312,"活动已经结束");
        }else if(sstate == 3){
            commonMessage = new CommonMessage(313,"用户未参加活动");
        }else{
            commonMessage = new CommonMessage(200,"修改用户状态成功");
        }
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/actrun/{token}/{activityId}")
    public String actRun(@PathVariable("token") String token,@PathVariable("activityId") String activityId) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        java.util.Date day=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(day));
        map.put("nowTime",sdf.format(day));
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("state",activityService.actRun(map));
        commonMessage = new CommonMessage(200,"返回成功",returnMap);
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/findact/{token}/{key}")
    public String findAct(@PathVariable("token") String token,@PathVariable("key") String key) throws Exception{
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("key",key);
        System.out.println(key);
        commonMessage = new CommonMessage(200,"返回成功",activityService.findAct(map));
        return JSON.toJSONString(commonMessage);
    }
}