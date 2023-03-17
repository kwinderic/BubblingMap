package com.bubbling.service;

import com.bubbling.mapper.UserMapper;
import com.bubbling.pojo.BubblingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    public List<BubblingUser> queryAllUser(){
        return userMapper.queryAllUser();
    }
}
