package com.albusxing.register.core;

import lombok.Data;

/**
 * 心跳返回对象
 * @author liguoqing
 */
@Data
public class HeartbeatResponse {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    /**
     * 心跳返回状态：success、failure
     */
    private String status;
}
