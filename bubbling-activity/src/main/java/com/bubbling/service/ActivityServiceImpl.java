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

//    @Override
//    public int addTask(Map<String,Object> map){
//
//    }

    @Override
    public int createTask(Map<String,Object> map){
        return activityMapper.addTask(map);
    }

    @Override
    public List<Map<String,Object>> showActivities(Map<String,Object> map){
        return activityMapper.showActivities(map);
    }

    @Override
    public int alterUserTaskState(Map<String,String> map){
        if(activityMapper.actRunState(map)!=2){
            return activityMapper.actRunState(map);
        }else{
            return activityMapper.alterUserTaskState(map);
        }
    }

}
