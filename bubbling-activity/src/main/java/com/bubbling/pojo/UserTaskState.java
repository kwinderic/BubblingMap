package com.bubbling.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户完成任务的状态
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserTaskState {
    private String activityId;
    private String userPhone;
    private String taskId;
    private int state;
}
