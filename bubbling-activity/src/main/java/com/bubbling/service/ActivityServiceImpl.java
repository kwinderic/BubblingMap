package com.bubbling.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.bubbling.mapper.ActivityMapper;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int recordUserLocation(Map<String, String> map) {
        return activityMapper.recordUserLocation(map);
    }

    @Override
    public List<Map<String, Object>> showActTask(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return null;
        }else{
            return activityMapper.showActTask(map);
        }
    }

//    @Override
//    public int addTask(Map<String,Object> map){
//
//    }

    @Override
    public int createTask(Map<String,Object> map){
        return activityMapper.addTask(map);
    }

    /**
     * 展示所有活动
     */
    @Override
    public List<Map<String,Object>> showActivities(Map<String,Object> map){
        return activityMapper.showActivities(map);
    }


    @Override
    public int alterUserTaskState(Map<String,Object> map) {
        map.forEach((k,v)-> System.out.println(k+" "+v));
        if (activityMapper.actRunState(map) != 0 && activityMapper.actRunState(map) != 2) {
            return 1;
        } else if (activityMapper.actRunState(map) != 2) {
            return 2;
        } else if (activityMapper.userPartiState(map) == 2 || activityMapper.userPartiState(map) == 3) {
            return 3;
        }
//        else if (activityMapper.userRunState(map) != 1) {
//            return 4;
//        }
        else {
            return -1 * activityMapper.alterUserTaskState(map);
        }
    }


    @Override
    public List<Map<String, Object>> activityRun(Map<String, Object> map) {
        List<Map<String,Object>> list = activityMapper.actShouldRun(map);
        activityMapper.actRun(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> activityFinish(Map<String, Object> map) {
        List<Map<String,Object>> list = activityMapper.actShouldFinish(map);
        activityMapper.actFinish(map);
        return list;
    }

    @Override
    public int createActivity(Map<String, Object> map) {
        map.put("activityId",activityMapper.activityNum(map)+1);
        return activityMapper.createActivity(map);
    }

    @Override
    public List<Map<String, Object>> showUserTaskState(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 2 || activityMapper.userPartiState(map) == 2){
            return null;
        }else {
            return activityMapper.showUserTaskState(map);
        }
    }

}
