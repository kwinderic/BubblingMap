package com.bubbling.service;

import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.pojo.PointOnMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {
    BubblingUser queryUserOnPhoneAndPassword(String userPhone, String password);
    String generateToken(String userPhone);
    Map<String,String> getUserBaseInfo(String userPhone);
    BubblingUserCard getUserCardInfo(String userPhone);
    List<Map<String,String>> getPartiActivityList(String userPhone);
    int setCard(String userPhone, BubblingUserCard card) throws Exception;
    int userPartiActivity(String userPhone, String activityId);
    int userQuitActivity(String userPhone, String activityId);
    ArrayList<PointOnMap> getNearbyActivity(String userPhone, double radius, long count);
}
