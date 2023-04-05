package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户实时位置
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActivityUserLocation {

    private String activityId;
    private String userPhone;
    private String longitude;
    private String latitude;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    private int state;
}
