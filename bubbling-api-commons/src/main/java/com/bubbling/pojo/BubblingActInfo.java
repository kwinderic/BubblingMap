package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 2022-03-03 15:36:20 GMT+8
 * 活动信息表对应实体类
 * @author k
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BubblingActInfo {
    private String activityNo;
    private String name;
    private String startTime;
    private String endTime;
    private String place;
    private int type;
    private String sponsorNo;
    private String sponsorNickname;
    private String sponsorPhone;
    private Date createTime;
    private Date updateTime;
    private int state;
}
