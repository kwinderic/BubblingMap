package com.bubbling.service;

import com.bubbling.mapper.AdminMapper;
import com.bubbling.mapper.UserMapper;
import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bubbling.utils.ReflectUtil.getObjectValue;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public BubblingUser UserLogin(String userPhone,String password){
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        BubblingUser bubblingUser = userMapper.queryUserByPhone(map);
        if(password.isEmpty() || !password.equals(bubblingUser.getPassword()))
            bubblingUser=null;
        return bubblingUser;
    }

    @Override
    public String generateToken(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return JWTUtil.getJWTToken(map);
    }

    @Override
    public Map<String,String> getUserInfo(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return userMapper.getUserInfo(map);
    }

    @Override
    public BubblingUserCard getCardInfo(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return userMapper.getCardInfo(map);
    }

    @Override
    public List<Map<String, String>> getUserActivityList(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", userPhone);
        return userMapper.getUserActivityList(map);
    }

    @Transactional
    @Override
    public int setCard(BubblingUserCard card) throws Exception {
        Map<String, Object> map;
        map=getObjectValue(card);
        userMapper.deleteCardInfo(map);
        return userMapper.insertCardInfo(map);
    }

    @Override
    public int userAddActivity(String partiNo, String activityNo) {
        Map<String, String> map = new HashMap<>();
        map.put("partiNo", partiNo);
        map.put("activityNo", activityNo);
        return userMapper.userAddActivity(map);
    }

    @Override
    public int userDeleteActivity(String partiNo, String activityNo) {
        Map<String, String> map = new HashMap<>();
        map.put("partiNo", partiNo);
        map.put("activityNo", activityNo);
        return userMapper.userDeleteActivity(map);
    }
}
