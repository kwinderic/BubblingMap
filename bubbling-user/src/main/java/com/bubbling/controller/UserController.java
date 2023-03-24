package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.service.UserService;
import com.bubbling.utils.JWTUtil;
import com.bubbling.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

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
    private final String onlineUser="onlineUser";
    private final String onlineActivity="onlineActivity";

    /**
     * 登录，并返回token
     * 2022-03-10 11:47:58 GMT+8
     * @author k
     */
    @PostMapping("/login/{userPhone}/{password}/{longitude}/{latitude}")
    public String login(@PathVariable("userPhone") String userPhone,@PathVariable("password") String password,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude){
        BubblingUser bubblingUser = userService.UserLogin(userPhone,password);
        if(bubblingUser!=null){
            String token=userService.generateToken(userPhone);
            commonMessage = new CommonMessage(200, "登录成功", token);
            redisUtil.geoAdd(onlineUser,new Point(Double.parseDouble(longitude),Double.parseDouble(latitude)),userPhone);
        }else commonMessage = new CommonMessage(201, "用户名或密码错误");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 登出，需要密码进行身份验证
     * 2022-03-12 20:39:19 GMT+8
     * @author k
     */
    @DeleteMapping("/logout/{token}/{password}")
    public String logout(@PathVariable("token") String token,@PathVariable("password") String password){
        String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
        BubblingUser bubblingUser = userService.UserLogin(userPhone,password);
        if(bubblingUser!=null){
            commonMessage = new CommonMessage(200, "退出成功");
            redisUtil.geoRemove(onlineUser, userPhone);
        } else commonMessage = new CommonMessage(201, "认证失败，退出失败");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取基本信息
     * 2022-03-10 11:48:15 GMT+8
     * @author k
     */
    @GetMapping("/getbaseinfo/{token}")
    public String getBaseInfo(@PathVariable("token") String token){
        System.out.println(798789789);
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            Map<String,String> map = userService.getUserInfo(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", map);
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

    /**
     * 获取token过期时间
     * 2022-03-13 21:12:18 GMT+8
     * @author k
     */
    @GetMapping("/gettimeouttime/{token}")
    public String getTimeoutTime(@PathVariable("token") String token){
        try {
            commonMessage = new CommonMessage(200, "还未过期", JWTUtil.verify(token).getClaim("exp"));
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

    /**
     * 根据token获取card信息
     * 2022-03-10 11:48:50 GMT+8
     * @author k
     */
    @GetMapping("/getcardinfo/{token}")
    public String getCardInfo(@PathVariable("token") String token){
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            BubblingUserCard card = userService.getCardInfo(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", card);
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

    /**
     * 根据token获取参与的活动的id列表
     * 2022-03-10 11:49:08 GMT+8
     * @author k
     */
    @GetMapping("/getactivitylist/{token}")
    public String getPartiActivityList(@PathVariable("token") String token){
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            List<Map<String, String>> list = userService.getPartiActivityList(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", list);
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

    /**
     * 根据token获取创建的活动的id列表
     * 2022-03-15 16:26:56 GMT+8
     * @author k
     */
    @GetMapping("/getcreateactivitylist/{token}")
    public String getCreateActivityList(@PathVariable("token") String token){
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            List<Map<String, String>> list = userService.getCreateActivityList(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", list);
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

    /**
     * 更改card
     * 2022-03-10 14:52:52 GMT+8
     * @author k
     */
    @PostMapping("/modifycard/{token}")
    public String modifyCard(@PathVariable("token") String token, BubblingUserCard card){
        try {
            JWTUtil.verify(token).getClaim("userPhone");
            if(userService.setCard(card)==1)
                commonMessage = new CommonMessage(210, "修改成功");
            else commonMessage = new CommonMessage(214, "修改失败");
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

    @PostMapping("/addactivity/{activityNo}/{token}")
    public String partiActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        try {
            String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            if(userService.userPartiActivity(userPhone, activityNo)==1)
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

    @PostMapping("/deleteactivity/{activityNo}/{token}")
    public String quitActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        try {
            String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            if(userService.userQuitActivity(userPhone, activityNo)==1)
                commonMessage = new CommonMessage(210, "删除成功");
            else commonMessage = new CommonMessage(214, "删除失败");
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
