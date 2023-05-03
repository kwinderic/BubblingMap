package com.bubbling.service;

import ch.qos.logback.core.db.dialect.MsSQLDialect;

import javax.jws.Oneway;
import java.util.List;
import java.util.Map;

public interface ActivityService {
    int recordUserLocation(Map<String,Object> map);
//    int eraseUserLocation(Map<String,String> map);
    List<Map<String,Object>> showUserLocation(Map<String,Object> map);
    List<Map<String,Object>> showAllUserLocation(Map<String,Object> map);
    List<Map<String,Object>> showActTask(Map<String,Object> map);
    int addTask(Map<String,Object> map);
    int eraseTask(Map<String,Object> map);
    int alterTask(Map<String,Object> map);
    int alterUserTaskState(Map<String,Object> map);
    List<Map<String,Object>> showActivities(Map<String,Object> map);
    int alterUserActProgress(Map<String,Object> map);
    List<Map<String,Object>> activityRun(Map<String,Object> map);
    List<Map<String,Object>> activityFinish(Map<String,Object> map);
//    int userEnterAct(Map<String,Object> map);
//    int userExitAct(Map<String,String> map);
//    int userSuspendAct(Map<String,String> map);
//    int userCompleteAct(Map<String,String> map);
    int createActivity(Map<String,Object> map,List<Map<String,Object>> maps);
    int dissolutionActivity(Map<String,Object> map);
    List<Map<String,Object>> showUserTaskState(Map<String,Object> map);
    int alterActInfo(Map<String,Object> map);
    int actRun(Map<String,Object> map);
    List<Map<String,Object>> findAct(Map<String,Object> map);
}
