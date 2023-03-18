package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bubbling.dto.CommonMessage;
import com.bubbling.pojo.BubblingUser;
import com.bubbling.pojo.BubblingUserCard;
import com.bubbling.service.UserService;
import com.bubbling.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(("/user"))
public class UserController {
    @Autowired
    private UserService userService;
    private CommonMessage commonMessage;

    /**
     * 登录，并返回token
     * 2022-03-10 11:47:58 GMT+8
     * @param userPhone
     * @param password
     * @return
     * @author k
     */
    @PostMapping("/login/{userPhone}/{password}")
    public String login(@PathVariable("userPhone") String userPhone,@PathVariable("password") String password){
        BubblingUser bubblingUser = userService.UserLogin(userPhone,password);
        if(bubblingUser!=null){
            String token=userService.generateToken(userPhone);
            commonMessage = new CommonMessage(200, "登录成功", token);
        }else commonMessage = new CommonMessage(201, "用户名或密码错误");
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取基本信息
     * 2022-03-10 11:48:15 GMT+8
     * @param token
     * @return
     * @author k
     */
    @GetMapping("/getbaseinfo/{token}")
    public String getBaseInfo(@PathVariable("token") String token){
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            Map<String,String> map = userService.getUserInfo(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", map);
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取card信息
     * 2022-03-10 11:48:50 GMT+8
     * @param token
     * @return
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
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 根据token获取参与/报名的活动列表
     * 2022-03-10 11:49:08 GMT+8
     * @param token
     * @return
     * @author k
     */
    @GetMapping("/getactivitylist/{token}")
    public String getActivityList(@PathVariable("token") String token){
        try {
            String userPhone=JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            List<Map<String, String>> list = userService.getUserActivityList(userPhone);
            commonMessage = new CommonMessage(210, "认证成功", list);
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    /**
     * 更改card
     * 2022-03-10 14:52:52 GMT+8
     * @param token
     * @param card
     * @return
     * @author k
     */
    @PostMapping("/modifycard/{token}")
    public String modifyCard(@PathVariable("token") String token, BubblingUserCard card){
        try {
            JWTUtil.verify(token).getClaim("userPhone");
            if(userService.setCard(card)==1)
                commonMessage = new CommonMessage(210, "修改成功");
            else commonMessage = new CommonMessage(210, "修改失败");
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/addactivity/{activityNo}/{token}")
    public String addActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        try {
            String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            userService.userDeleteActivity(userPhone, activityNo);
            commonMessage = new CommonMessage(210, "添加成功");
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }

    @PostMapping("/deleteactivity/{activityNo}/{token}")
    public String deleteActivity(@PathVariable("token") String token,@PathVariable("activityNo") String activityNo){
        try {
            String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
            userService.userDeleteActivity(userPhone, activityNo);
            commonMessage = new CommonMessage(210, "删除成功");
        }catch (TokenExpiredException e){
            commonMessage = new CommonMessage(211, "令牌过期，认证失败");
        }catch (SignatureGenerationException e){
            commonMessage = new CommonMessage(212, "签名不一致，认证失败");
        }catch (Exception e){
            e.printStackTrace();
            commonMessage = new CommonMessage(213, "未知异常，认证失败");
        }
        return JSON.toJSONString(commonMessage);
    }
}
