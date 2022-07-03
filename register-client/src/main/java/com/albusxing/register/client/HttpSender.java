package com.albusxing.register.client;

import com.albusxing.register.core.HeartbeatRequest;
import com.albusxing.register.core.HeartbeatResponse;
import com.albusxing.register.core.RegisterRequest;
import com.albusxing.register.core.RegisterResponse;

/**
 * @author Albusxing
 * @created 2022/6/25
 */
public class HttpSender {


    public static RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        response.setStatus(RegisterResponse.SUCCESS);
        return response;
    }

    public static HeartbeatResponse heartbeat(HeartbeatRequest request) {
        HeartbeatResponse response = new HeartbeatResponse();
        response.setStatus(RegisterResponse.SUCCESS);
        return response;
    }
}
