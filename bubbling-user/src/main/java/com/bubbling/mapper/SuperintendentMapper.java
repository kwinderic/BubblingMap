package com.bubbling.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SuperintendentMapper {
    List<Map<String,String>> getCreateActivityList(Map<String,String> map);
    int superintendentCreateActivity(Map<String,String> map);
    int superintendentDeleteActivity(Map<String,String> map);
    List<Map<String,String>> getUserApplyList(Map<String,String> map);
    int superintendentPassApply(Map<String,String> map);
    int superintendentRejectApply(Map<String,String> map);
}
