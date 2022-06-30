package com.albusxing.register.core;

import lombok.Data;

/**
 * 注册请求对象
 * @author liguoqing
 */
@Data
public class RegisterRequest {

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务实例id
     */
    private String serviceInstanceId;
    /**
     * 主机名
     */
    private String hostname;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;


}
