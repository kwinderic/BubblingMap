package com.bubbling.service;

import com.bubbling.mapper.UserMapper;
import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.pojo.PointOnMap;
import com.bubbling.utils.ConstantUtil;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.RedisUtil;
import com.bubbling.utils.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BubblingUser queryUserOnPhoneAndPassword(String userPhone,String password){
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        BubblingUser bubblingUser = userMapper.queryUserByPhone(map);
        if(bubblingUser!=null || !password.isEmpty() || password.equals(bubblingUser.getPassword()))
            return bubblingUser;
        return null;
    }

    @Override
    public String generateToken(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return JWTUtil.getJWTToken(map);
    }

    @Override
    public Map<String,String> getUserBaseInfo(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return userMapper.getUserBaseInfo(map);
    }

    @Override
    public BubblingUserCard getUserCardInfo(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return userMapper.getUserCardInfo(map);
    }

    @Override
    public List<Map<String, String>> getPartiActivityList(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return userMapper.getPartiActivityList(map);
    }

    /**
     * 开启事务
     * 2022-03-19 21:08:32 GMT+8
     * @author k
     */
    @Transactional
    @Override
    public int setCard(String userPhone, BubblingUserCard card) throws Exception {
        Map<String, Object> map;
        map=ReflectUtil.getObjectValue(card);
        if(map.get(userPhone)!=null)
            map.replace("userPhone", userPhone);
        else map.put("userPhone", userPhone);
        userMapper.deleteCardInfo(map);
        return userMapper.insertCardInfo(map);
    }

    @Override
    public int userPartiActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return userMapper.userPartiActivity(map);
    }

    @Override
    public int userQuitActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        return userMapper.userQuitActivity(map);
    }

    @Override
    public ArrayList<PointOnMap> getNearbyActivity(String userPhone, double radius, long count) {
        Distance distance = new Distance(radius, Metrics.KILOMETERS);
        ArrayList<PointOnMap> lists=new ArrayList<>();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisUtil.geoRadiusByMember(ConstantUtil.onlineActivity, userPhone, distance,count);
        results.forEach(item -> {
            RedisGeoCommands.GeoLocation<Object> location = item.getContent();
            Point point = location.getPoint();
            lists.add(new PointOnMap(location.getName().toString(),point.getX(),point.getY(),item.getDistance()));
        });
        return lists;
    }
}
