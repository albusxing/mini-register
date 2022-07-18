package com.albusxing.register.client;

import com.albusxing.register.core.HeartbeatRequest;
import com.albusxing.register.core.HeartbeatResponse;
import com.albusxing.register.core.RegisterRequest;
import com.albusxing.register.core.RegisterResponse;

/**
 * 发送http请求
 * @author Albusxing
 * @created 2022/6/25
 */
public class HttpSender {


    /**
     * 发送服务注册请求
     * @param request
     * @return
     */
    public static RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        response.setStatus(RegisterResponse.SUCCESS);
        return response;
    }

    /**
     * 发送心跳请求
     * @param request
     * @return
     */
    public static HeartbeatResponse heartbeat(HeartbeatRequest request) {
        HeartbeatResponse response = new HeartbeatResponse();
        response.setStatus(RegisterResponse.SUCCESS);
        return response;
    }
}
