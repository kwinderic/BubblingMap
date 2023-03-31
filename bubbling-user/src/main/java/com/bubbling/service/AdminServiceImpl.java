package com.bubbling.service;

import com.bubbling.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public int adminAddActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return adminMapper.adminCreateActivity(map);
    }

    @Override
    public int adminDeleteActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return adminMapper.adminDeleteActivity(map);
    }

    @Override
    public List<Map<String, String>> getAdminActivityList(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return adminMapper.getCreateActivityList(map);
    }
}
