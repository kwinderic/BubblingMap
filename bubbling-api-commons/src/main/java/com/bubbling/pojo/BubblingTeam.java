package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 2022-03-03 15:44:24 GMT+8
 * 团体信息对应实体类
 * @author k
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BubblingTeam {
    private String teamId;
    private String teamName;
    private int type;
    private String intro;
    private Date createTime;
    private Date updateTime;
    private int state;
}
