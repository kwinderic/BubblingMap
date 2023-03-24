package com.bubbling.service;

import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;

import java.util.List;
import java.util.Map;

public interface UserService {
    BubblingUser UserLogin(String userPhone, String password);
    String generateToken(String userPhone);
    Map<String,String> getUserInfo(String userPhone);
    BubblingUserCard getCardInfo(String userPhone);
    List<Map<String,String>> getPartiActivityList(String userPhone);
    List<Map<String,String>> getCreateActivityList(String userPhone);
    int setCard(BubblingUserCard card) throws Exception;
    int userPartiActivity(String partiNo, String activityNo);
    int userQuitActivity(String partiNo, String activityNo);
}
