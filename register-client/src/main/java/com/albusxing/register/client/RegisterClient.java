package com.albusxing.register.client;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * 负责与register-server通信
 * @author liguoqing
 */
public class RegisterClient {

    /**
     * 服务实例id
     */
    private final String serviceInstanceId;

    public RegisterClient() {
        this.serviceInstanceId = UUID.randomUUID().toString();
    }

    public void start() {
        new RegisterClientWorker(serviceInstanceId).start();
    }
}
