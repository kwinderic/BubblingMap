package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.service.TeamService;
import com.bubbling.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(("/team"))
public class TeamController {
    @Autowired
    private TeamService teamService;
    private CommonMessage commonMessage;

    @PostMapping("/addteam/{token}/{teamName}")
    public String addTeam(@PathVariable("token") String token,@PathVariable("teamName") String teamName,String intro){
        try {
            String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            if(teamService.addTeam(userPhone,teamName,intro)==1)
                commonMessage = new CommonMessage(210, "添加成功");
            else commonMessage = new CommonMessage(214, "添加失败");
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/getteamlist/{token}")
    public String getTeamList(@PathVariable("token") String token){
        try {
            String userPhone= JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            commonMessage = new CommonMessage(210, "认证成功",teamService.getTeamList(userPhone));
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }
}
