package com.bubbling.mapper;

import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    BubblingUser queryUserByPhone(Map<String,String> map);
    Map<String,String> getUserInfo(Map<String,String> map);
    BubblingUserCard getCardInfo(Map<String,String> map);
    List<Map<String,String>> getPartiActivityList(Map<String,String> map);
    int insertCardInfo(Map<String,Object> map);
    int deleteCardInfo(Map<String,Object> map);
    int userPartiActivity(Map<String,String> map);
    int userQuitActivity(Map<String,String> map);
}
