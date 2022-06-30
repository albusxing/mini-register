package com.albusxing.register.core;

import lombok.Data;

/**
 * 心跳请求对象
 * @author liguoqing
 */
@Data
public class HeartbeatRequest {

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务实例id
     */
    private String serviceInstanceId;

}
