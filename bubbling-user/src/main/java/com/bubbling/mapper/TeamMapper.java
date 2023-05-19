package com.bubbling.mapper;

import com.bubbling.pojo.BubblingTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeamMapper {
    int createTeam(Map<String,String> map);
    List<BubblingTeam> getTeamList(Map<String,String> map);
}
