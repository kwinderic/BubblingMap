package com.bubbling.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name="User-Module-Service")
public interface UserServiceConsumer {
    @PostMapping("/user/login/{userPhone}/{password}/{longitude}/{latitude}")
    String login(@PathVariable("userPhone") String userPhone, @PathVariable("password") String password, @PathVariable("longitude") String longitude, @PathVariable("latitude") String latitude);

//    @PostMapping("/user/partiactivity/{token}/{activityId}")
//    String partiActivity(@PathVariable("token") String token,@PathVariable("activityId") String activityId);

    @PostMapping("/admin/addactivity/{token}/{activityNo}/{longitude}/{latitude}")
    String addActivity(@PathVariable("token") String token, @PathVariable("activityNo") String activityNo,@PathVariable("longitude") String longitude,@PathVariable("latitude") String latitude);
}


