package com.bubbling.service;

import com.bubbling.mapper.TeamMapper;
import com.bubbling.pojo.BubblingTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamServiceImpl implements TeamService{
    @Autowired
    private TeamMapper teamMapper;

    @Override
    public int addTeam(String teamId,String teamName,String intro) {
        Map<String, String> map = new HashMap<>();
        map.put("teamId",teamId);
        map.put("teamName",teamName);
        map.put("intro",intro);
        return teamMapper.createTeam(map);
    }

    @Override
    public List<BubblingTeam> getTeamList(String userPhone) {
        Map<String, String> map = new HashMap<>();
        map.put("userPhone",userPhone);
        return teamMapper.getTeamList(map);
    }
}
