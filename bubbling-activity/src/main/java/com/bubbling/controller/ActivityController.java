package com.bubbling.controller;

import com.bubbling.service.UserServiceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {
    @Autowired
    private UserServiceConsumer userServiceConsumer;

    @RequestMapping("/test")
    public String test(){
        return userServiceConsumer.login("11111","123456","123.45","67.89");
    }
}
