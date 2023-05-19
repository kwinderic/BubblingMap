package com.bubbling.service;

import java.util.List;
import java.util.Map;

public interface SuperintendentService {
    int superintendentCreateActivity(String partiId, String activityId);
    int superintendentDeleteActivity(String partiId, String activityId);
    List<Map<String,String>> getSuperintendentActivityList(String userPhone);
    List<Map<String,String>> getUserApplyList(String activityId);
    int superintendentPassApply(String userPhone, String activityId);
    int superintendentRejectApply(String userPhone, String activityId);
}
