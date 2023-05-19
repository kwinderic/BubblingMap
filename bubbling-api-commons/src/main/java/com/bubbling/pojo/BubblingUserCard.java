package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 2022-03-03 15:30:49 GMT+8
 * 用户信息卡片对应实体类
 * @author k
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BubblingUserCard {
    private String  userPhone;
    private String  realName;
    private char    gender;
    private String  identityType;
    private String  identityNo;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date    birthday;
    private String  area;
    private String  relation;
    private String  emergencyPhone;
    private Date    createTime;
    private Date    updateTime;
    private int     state;
}
