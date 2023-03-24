package com.bubbling.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    List<Map<String,String>> getCreateActivityList(Map<String,String> map);
    int adminCreateActivity(Map<String,String> map);
    int adminDeleteActivity(Map<String,String> map);
}
