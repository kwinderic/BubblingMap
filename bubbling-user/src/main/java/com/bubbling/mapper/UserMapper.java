package com.bubbling.mapper;

import com.bubbling.pojo.BubblingUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<BubblingUser> queryAllUser();
}
