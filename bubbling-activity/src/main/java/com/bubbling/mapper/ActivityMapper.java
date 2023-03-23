package com.bubbling.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper {

    int recordUserLocation(Map<String, String> map);
    int eraseUserLocation(Map<String,String> map);
    List<Map<String,String>> queryUserLocation(Map<String,String> map);
    List<Map<String,String>> queryAllUserLocation(Map<String,String> map);
    int addTask(Map<String,Object> map);
    int eraseTask(Map<String,String> map);
    int alterTask(Map<String,String> map);
    int alterUserTaskState(Map<String,String> map);
    int alterUserActState(Map<String,String> map);
    int actRunState(Map<String,String> map);
    int userPatiState(Map<String,Object> map);
    int userRunState(Map<String,String> map);
    List<Map<String,String>> actShouldRun(Map<String,String> map);
    int actRun(Map<String,String> map);
    List<Map<String,String>> actShouldFinish(Map<String,String> map);
    int actFinish(Map<String,String> map);
    int userEnterAct(Map<String,String> map);
    int userExitAct(Map<String,String> map);
    int userSuspendAct(Map<String,String> map);
    int userFinishAct(Map<String,String> map);
    int userContinueAct(Map<String,String> map);
    List<Map<String,Object>> showActivities(Map<String,Object> map);
}
