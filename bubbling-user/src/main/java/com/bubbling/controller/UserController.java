package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.pojo.PointOnMap;
import com.bubbling.service.UserService;
import com.bubbling.utils.ConstantUtil;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(("/user"))
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    private CommonMessage commonMessage;

    /**
     * 登录，并返回token
     * 2022-03-19 10:38:33 GMT+8
     * @author k
     */
    @PostMapping("/login/{userPhone}/{password}/{longitude}/{latitude}")
    public String login(@PathVariable("userPhone") String userPhone,@PathVariable("password") String password,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude){
        BubblingUser bubblingUser = userService.queryUserOnPhoneAndPassword(userPhone,password);
        if(bubblingUser!=null){
            String token=userService.generateToken(userPhone);
            redisUtil.geoAdd(ConstantUtil.onlineUser,new Point(Double.parseDouble(longitude),Double.parseDouble(latitude)),userPhone);
            commonMessage = new CommonMessage(200, "登录成功", token);
        }else commonMessage = new CommonMessage(201, "用户名或密码错误");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 登出，需要密码进行身份验证
     * 2022-03-19 10:38:37 GMT+8
     * @author k
     */
    @DeleteMapping("/logout/{token}/{password}")
    public String logout(@PathVariable("token") String token,@PathVariable("password") String password){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        BubblingUser bubblingUser = userService.queryUserOnPhoneAndPassword(userPhone,password);
        if(bubblingUser!=null){
            commonMessage = new CommonMessage(200, "退出成功");
            redisUtil.geoRemove(ConstantUtil.onlineUser, userPhone);
        } else commonMessage = new CommonMessage(201, "认证失败，退出失败");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取基本信息
     * 2022-03-19 10:38:39 GMT+8
     * @author k
     */
    @GetMapping("/getbaseinfo/{token}")
    public String getBaseInfo(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        Map<String,String> map = userService.getUserInfo(userPhone);
        commonMessage = new CommonMessage(210, "获取成功", map);
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 获取token过期时间
     * 2022-03-19 10:38:42 GMT+8
     * @author k
      */
    @GetMapping("/gettimeouttime/{token}")
    public String getTimeoutTime(@PathVariable("token") String token){
        commonMessage = new CommonMessage(200, "token未过期", JWTUtil.verify(token).getClaim("exp"));
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/update2ltude/{token}/{longitude}/{latitude}")
    public String update2LTude(@PathVariable("token") String token,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(redisUtil.geoPos(ConstantUtil.onlineUser, userPhone)!=null){
            redisUtil.geoAdd(ConstantUtil.onlineUser,new Point(Double.parseDouble(longitude),Double.parseDouble(latitude)),userPhone);
            commonMessage = new CommonMessage(210, "位置已更新", token);
        }else commonMessage = new CommonMessage(211, "不在登录状态");
        return JSON.toJSONString(commonMessage);
    }

    @GetMapping("/getnearbyactivity/{token}")
    public String getNearbyActivityList(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        List<Point> points=redisUtil.geoPos(ConstantUtil.onlineUser, userPhone);
        if(points!=null){
            ArrayList<PointOnMap> lists = userService.getNearbyActivity(userPhone, 40,10);
            commonMessage = new CommonMessage(210, "获取成功", lists);
        }else commonMessage = new CommonMessage(211, "不在登录状态");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取card信息
     * 2022-03-19 10:38:44 GMT+8
     * @author k
     */
    @GetMapping("/getcardinfo/{token}")
    public String getCardInfo(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        BubblingUserCard card = userService.getCardInfo(userPhone);
        commonMessage = new CommonMessage(210, "获取成功", card);
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取参与的活动的id列表
     * 2022-03-19 10:38:48 GMT+8
     * @author k
     */
    @GetMapping("/getactivitylist/{token}")
    public String getPartiActivityList(@PathVariable("token") String token){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        List<Map<String, String>> list = userService.getPartiActivityList(userPhone);
        commonMessage = new CommonMessage(210, "获取成功", list);
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 更改card, 若抛出异常, 则http返回状态码为500
     * 2022-03-19 10:38:53 GMT+8
     * @author k
     */
    @PostMapping("/modifycard/{token}")
    public String modifyCard(@PathVariable("token") String token, BubblingUserCard card) throws Exception{
            JWTUtil.verify(token).getClaim("userPhone");
            if(userService.setCard(card)==1)
            commonMessage = new CommonMessage(210, "修改成功");
        else commonMessage = new CommonMessage(211, "修改失败");
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/partiactivity/{activityNo}/{token}")
    public String partiActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(userService.userPartiActivity(userPhone, activityNo)==1)
            commonMessage = new CommonMessage(210, "添加成功");
        else commonMessage = new CommonMessage(211, "添加失败");
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/quitactivity/{activityNo}/{token}")
    public String quitActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        if(userService.userQuitActivity(userPhone, activityNo)==1)
            commonMessage = new CommonMessage(210, "删除成功");
        else commonMessage = new CommonMessage(211, "删除失败");
        return JSON.toJSONString(commonMessage);
    }
}
