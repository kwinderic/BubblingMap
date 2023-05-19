package com.bubbling.service;

import com.bubbling.pojo.BubblingTeam;

import java.util.List;

public interface TeamService {
    int addTeam(String teamId,String teamName,String intro);
    List<BubblingTeam> getTeamList(String userPhone);
}
