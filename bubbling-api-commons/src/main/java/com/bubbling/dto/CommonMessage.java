package com.bubbling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2022-02-26 17:17:31 GMT+8
 * 前后端交换信息的消息类，以json格式
 * @param <T> data数据的泛型
 * @author k
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonMessage<T> {
    private int code;	//状态码
    private String msg;	//消息内容
    private T data;	    //具体数据

    /**
     * 2022-02-26 17:19:35 GMT+8
     * 无data的构造方法
     * @param code
     * @param message
     * @author k
     */
    public CommonMessage(int code, String message){
        this(code, message, null);
    }
}