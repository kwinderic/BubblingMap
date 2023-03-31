package com.bubbling.service;

import java.util.List;
import java.util.Map;

public interface AdminService {
    int adminAddActivity(String partiId, String activityId);
    int adminDeleteActivity(String partiId, String activityId);
    List<Map<String,String>> getAdminActivityList(String userPhone);
}
