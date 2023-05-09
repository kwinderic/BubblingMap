package com.bubbling.service;

import com.bubbling.mapper.UserMapper;
import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.pojo.PointOnMap;
import com.bubbling.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SendSMSUtil sendSMSUtil;
    @Value("${tencent-cloud.secretId}")
    private String secretId;
    @Value("${tencent-cloud.secretKey}")
    private String secretKey;

    /**
     * 发送验证码，并获取发送后的信息
     * 【已测试】
     * 2022-03-24 11:07:34 GMT+8
     * @throws Exception
     * @author k
     */
    @Override
    public String sendVerificationCode(String userPhone) throws Exception {
        Random random = new Random();
        String verificationCode=String.valueOf(random.nextInt(9000)+1000);

        // 放置在redis缓存中，并设置过期时间
        redisUtil.set(ConstantUtil.verificationCode+":"+userPhone, verificationCode);
        redisUtil.expire(ConstantUtil.verificationCode+":"+userPhone, 180);

        // 发送验证码，并记录发送短信的相关信息
        String result= sendSMSUtil.sendVerificationCode(secretId,secretKey,userPhone,verificationCode);
        log.info("[tencent cloud] result: "+result);
        sendSMSUtil.recordSendSMS(result, verificationCode);
        return result;
    }

    @Override
    public int createUser(String userPhone, String password, String nickname) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("password", password);
        map.put("nickname", nickname);
        return userMapper.createUser(map);
    }

    @Override
    public BubblingUser queryUserOnPhoneAndPassword(String userPhone,String password){
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        BubblingUser bubblingUser = userMapper.queryUserByPhone(map);
        if(bubblingUser!=null && !password.isEmpty() && password.equals(bubblingUser.getPassword()))
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
        BubblingUserCard userCardInfo = userMapper.getUserCardInfo(map);
        return userCardInfo;
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
        map.put("userPhone", userPhone);
        userMapper.deleteCardInfo(map);
        return userMapper.insertCardInfo(map);
    }

    @Override
    public int userPartiActivity(String userPhone, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        map.put("activityId", activityId);
        List<Map<String, String>> lists = userMapper.getPartiActivityList(map);
        for (Map<String, String> list : lists) {
            if(list.get("activityid").equals(activityId))
                return 0;
        }
        lists=userMapper.getUnderReviewActivityList(map);
        for (Map<String, String> list : lists) {
            if(list.get("activityid").equals(activityId))
                return -1;
        }
        if(userMapper.userPartiActivity2(map)==1)
            return 1;
        return userMapper.userPartiActivity1(map);
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

    @Override
    public List<Map<String, String>> getInApplyList(String userPhone) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userPhone",userPhone);
        return userMapper.getInApplyList(map);
    }

    @Override
    public int isInCharge(String userPhone, String activityId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userPhone",userPhone);
        map.put("activityId",activityId);
        if(!userMapper.isInCharge(map).isEmpty())   return 1;
        return 0;
    }
}
