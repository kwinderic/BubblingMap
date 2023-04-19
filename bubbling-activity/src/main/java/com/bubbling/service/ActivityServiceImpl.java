package com.bubbling.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.bubbling.mapper.ActivityMapper;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private ActivityMapper activityMapper;

    @Transactional
    @Override
    public int recordUserLocation(Map<String, Object> map) {
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
            return -1 * activityMapper.recordUserLocation(map);
        }
    }

    @Transactional
    @Override
    public List<Map<String, Object>> showActTask(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return null;
        }else{
            return activityMapper.showActTask(map);
        }
    }

    @Transactional
    @Override
    public int addTask(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return 1;
        }else if(activityMapper.actRunState(map) == 3){
            return 2;
        }else if(activityMapper.userPartiState(map) != 1){
            return 3;
        }else {
            return -1*activityMapper.addTask(map);
        }
    }

    @Transactional
    @Override
    public int eraseTask(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return 1;
        }else if(activityMapper.actRunState(map) == 3){
            return 2;
        }else if(activityMapper.userPartiState(map) != 1){
            return 3;
        }else {
            return -1*activityMapper.eraseTask(map);
        }
    }

    @Transactional
    @Override
    public int alterTask(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return 1;
        }else if(activityMapper.actRunState(map) == 3){
            return 2;
        }else if(activityMapper.userPartiState(map) != 1){
            return 3;
        }else {
            return -1*activityMapper.alterTask(map);
        }
    }

//    @Override
//    public int addTask(Map<String,Object> map){
//
//    }


    /**
     * 展示所有活动
     */
    @Override
    public List<Map<String,Object>> showActivities(Map<String,Object> map){
        return activityMapper.showActivities(map);
    }

    @Transactional
    @Override
    public int alterUserActProgress(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return 1;
        }else if(activityMapper.actRunState(map) == 3){
            return 2;
        }else{
            return -1*activityMapper.alterUserActProgress(map);
        }
    }


    @Transactional
    @Override
    public int alterUserTaskState(Map<String,Object> map) {
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


    @Transactional
    @Override
    public List<Map<String, Object>> activityRun(Map<String, Object> map) {
        List<Map<String,Object>> list = activityMapper.actShouldRun(map);
        activityMapper.actRun(map);
        return list;
    }

    @Transactional
    @Override
    public List<Map<String, Object>> activityFinish(Map<String, Object> map) {
        List<Map<String,Object>> list = activityMapper.actShouldFinish(map);
        activityMapper.actFinish(map);
        return list;
    }

    @Transactional
    @Override
    public int createActivity(Map<String, Object> map, List<Map<String, Object>> maps) {
        int newActivityId = activityMapper.activityNum(map)+1;
        map.put("activityId",newActivityId);
        activityMapper.createActivity(map);
        for (Map<String, Object> stringObjectMap : maps) {
            stringObjectMap.put("activityId",newActivityId);
        }
        activityMapper.createTask(maps);
        return 0;
    }

    @Override
    public int dissolutionActivity(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return 1;
        }else if(activityMapper.userPartiState(map) != 1){
            return 2;
        }else{
            return -1*activityMapper.dissolutionActivity(map);
        }
    }


    @Transactional
    @Override
    public List<Map<String, Object>> showUserTaskState(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 2 || activityMapper.userPartiState(map) == 2){
            return null;
        }else {
            return activityMapper.showUserTaskState(map);
        }
    }

    @Transactional
    @Override
    public int alterActInfo(Map<String, Object> map) {
        if(activityMapper.actRunState(map) == 1){
            return 1;
        }else if(activityMapper.actRunState(map) == 3){
            return 2;
        }else if(activityMapper.userPartiState(map) != 1){
            return 3;
        }else {
            return -1*activityMapper.alterActInfo(map);
        }
    }

    @Transactional
    @Override
    public List<Map<String,Object>> showUserLocation(Map<String,Object> map){
        return activityMapper.showUserLocation(map);
    }

    @Transactional
    @Override
    public List<Map<String, Object>> showAllUserLocation(Map<String, Object> map) {
        if(activityMapper.userPartiState(map) != 1){
            return null;
        }else{
            return activityMapper.showAllUserLocation(map);
        }
    }


}
