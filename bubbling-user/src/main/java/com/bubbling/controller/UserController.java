package com.bubbling.controller;

import com.alibaba.fastjson.JSON;
import com.bubbling.Service.UserService;
import com.bubbling.dto.CommonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public String test(){
        CommonMessage commonMessage = new CommonMessage(400, "user服务", userService.queryAllUser());
        return JSON.toJSONString(commonMessage);
    }
}
