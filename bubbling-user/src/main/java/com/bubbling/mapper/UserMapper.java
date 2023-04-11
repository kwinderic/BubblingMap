package com.bubbling.mapper;

import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    int createUser(Map<String,String> map);
    int insertSendVerificationCodeRecord(Map<String,Object> map);
    BubblingUser queryUserByPhone(Map<String,String> map);
    Map<String,String> getUserBaseInfo(Map<String,String> map);
    BubblingUserCard getUserCardInfo(Map<String,String> map);
    List<Map<String,String>> getPartiActivityList(Map<String,String> map);
    List<Map<String,String>> getUnderReviewActivityList(Map<String,String> map);
    int insertCardInfo(Map<String,Object> map);
    int deleteCardInfo(Map<String,Object> map);
    int userPartiActivity(Map<String,String> map);
    int userQuitActivity(Map<String,String> map);
}
