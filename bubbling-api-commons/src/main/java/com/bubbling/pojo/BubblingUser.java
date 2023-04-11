package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 2022-02-27 14:35:44 GMT+8
 * 用户对应实体类
 * @author k
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BubblingUser {
    private String userPhone;
    private String nickname;
    private String password;
    private Date createTime;
    private Date updateTime;
    private int state;
    private int authority;
}
