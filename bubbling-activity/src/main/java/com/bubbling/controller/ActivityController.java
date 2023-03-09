package com.bubbling.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
