package com.bubbling.service;

import com.bubbling.mapper.SuperintendentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SuperintendentServiceImpl implements SuperintendentService {
    @Autowired
    private SuperintendentMapper superintendentMapper;

    @Override
    public int superintendentCreateActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return superintendentMapper.superintendentCreateActivity(map);
    }

    @Override
    public int superintendentDeleteActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return superintendentMapper.superintendentDeleteActivity(map);
    }

    @Override
    public List<Map<String, String>> getSuperintendentActivityList(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return superintendentMapper.getCreateActivityList(map);
    }

    @Override
    public List<Map<String, String>> getUserApplyList(String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);
        return superintendentMapper.getUserApplyList(map);
    }

    @Override
    public int superintendentPassApply(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return superintendentMapper.superintendentPassApply(map);
    }

    @Override
    public int superintendentRejectApply(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return superintendentMapper.superintendentRejectApply(map);
    }
}
