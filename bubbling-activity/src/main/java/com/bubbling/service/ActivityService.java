package com.bubbling.service;

import ch.qos.logback.core.db.dialect.MsSQLDialect;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int recordUserLocation(Map<String,String> map);
//    int eraseUserLocation(Map<String,String> map);
//    List<Map<String,String>> queryUserLocation(Map<String,String> map);
//    List<Map<String,String>> queryAllUserLocation(Map<String,String> map);
//    int addTask(Map<String,Object> map);
//    int eraseTask(Map<String,String> map);
//    int alterTask(Map<String,String> map);
    int alterUserTaskState(Map<String,String> map);
    int createTask(Map<String,Object> map);
    List<Map<String,Object>> showActivities(Map<String,Object> map);
//    List<Map<String,String>> alterUserActProgress(Map<String,String> map);
//    List<Map<String,String>> activityRun(Map<String,String> map);
//    List<Map<String,String>> activityFinish(Map<String,String> map);
//    int userEnterAct(Map<String,String> map);
//    int userExitAct(Map<String,String> map);
//    int userSuspendAct(Map<String,String> map);
//    int userCompleteAct(Map<String,String> map);
}
