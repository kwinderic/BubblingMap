package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.service.TeamService;
import com.bubbling.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(("/team"))
public class TeamController {
    @Autowired
    private TeamService teamService;
    private CommonMessage commonMessage;

    @PostMapping("/addteam/{token}/{teamName}")
    public String addTeam(@PathVariable("token") String token,@PathVariable("teamName") String teamName,String intro){
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(teamService.addTeam(userPhone,teamName,intro)==1)
            commonMessage = new CommonMessage(210, "添加成功");
        else commonMessage = new CommonMessage(211, "添加失败");
        return JSON.toJSONString(commonMessage);
    }

    @DeleteMapping("/quitteam/{token}/{teamId}")
    public String quitTeam(@PathVariable("token") String token,@PathVariable("teamId") String teamId,String intro){
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(teamService.addTeam(userPhone,teamId,intro)==1)
            commonMessage = new CommonMessage(210, "添加成功");
        else commonMessage = new CommonMessage(211, "添加失败");
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/getteamlist/{token}")
    public String getTeamList(@PathVariable("token") String token){
        String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        commonMessage = new CommonMessage(210, "获取成功",teamService.getTeamList(userPhone));
        return JSON.toJSONString(commonMessage);
    }
}
