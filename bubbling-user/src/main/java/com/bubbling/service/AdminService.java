package com.bubbling.service;

import java.util.List;
import java.util.Map;

public interface AdminService {
    int adminAddActivity(String partiNo, String activityNo);
    int adminDeleteActivity(String partiNo, String activityNo);
    List<Map<String,String>> getAdminActivityList(String userPhone);
}
