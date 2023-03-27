package com.bubbling.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 活动任务
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActivityTask {
    private String activityId;
    private String taskId;
    private String premise;
    private int state;
}
