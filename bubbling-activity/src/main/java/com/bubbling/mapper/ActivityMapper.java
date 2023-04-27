package com.bubbling.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.w3c.dom.ls.LSInput;

import javax.management.ObjectName;
import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper {

    List<Map<String,Object>> showActivities(Map<String,Object> map);
    int createActivity(Map<String,Object> map);
    int dissolutionActivity(Map<String,Object> map);
    int activityNum(Map<String,Object> map);
    int recordUserLocation(Map<String,Object> map);
    int eraseUserLocation(Map<String,String> map);
    List<Map<String,Object>> showUserLocation(Map<String,Object> map);
    List<Map<String,Object>> showAllUserLocation(Map<String,Object> map);
    List<Map<String,Object>> showActTask(Map<String,Object> map);
    int addTask(Map<String,Object> map);
    int createTask(List<Map<String,Object>> maps);
    int eraseTask(Map<String,Object> map);
    int alterTask(Map<String,Object> map);
    int alterUserTaskState(Map<String, Object> map);
    int alterUserActProgress(Map<String,Object> map);
    int actRunState(Map<String,Object> map);
    Integer userPartiState(Map<String,Object> map);
    int userRunState(Map<String,Object> map);
    List<Map<String,Object>> actShouldRun(Map<String,Object> map);
    int actRun(Map<String, Object> map);
    List<Map<String,Object>> actShouldFinish(Map<String, Object> map);
    int actFinish(Map<String,Object> map);
    int userEnterAct(Map<String,String> map);
    int userExitAct(Map<String,String> map);
    int userSuspendAct(Map<String,String> map);
    int userFinishAct(Map<String,String> map);
    int userContinueAct(Map<String,String> map);
    List<Map<String,Object>> showUserTaskState(Map<String,Object> map);
    int alterActInfo(Map<String, Object> map);
    int insertUserTaskState(List<Map<String,Object>> maps);
    int insertUserActProgress(Map<String,Object> map);
    Integer queryActProgress(Map<String,Object> map);
    List<String> queryActTask(Map<String,Object> map);
    List<String> queryUserTaskState(Map<String,Object> map);
}
