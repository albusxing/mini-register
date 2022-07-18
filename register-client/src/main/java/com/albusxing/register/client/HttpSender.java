package com.albusxing.register.client;

import com.albusxing.register.core.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送http请求
 * @author Albusxing
 * @created 2022/6/25
 */
@Slf4j
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

    /**
     * 拉取服务注册表
     * @return
     */
    public static Map<String, Map<String, ServiceInstance>> fetchServiceRegistry() {
        Map<String, Map<String, ServiceInstance>> registry = new HashMap<>();

        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setHostname("finance-service-01");
        serviceInstance.setIp("192.168.31.1207");
        serviceInstance.setPort(9000);
        serviceInstance.setServiceInstanceId("FINANCE-SERVICE-192.168.31.207:9000");
        serviceInstance.setServiceName("FINANCE-SERVICE");

        Map<String, ServiceInstance> serviceInstances = new HashMap<>();
        serviceInstances.put("FINANCE-SERVICE-192.168.31.207:9000", serviceInstance);

        registry.put("FINANCE-SERVICE", serviceInstances);

        log.info("拉取注册表：" + registry);

        return registry;
    }

    /**
     * 服务下线
     * @param serviceName
     * @param serviceInstanceId
     */
    public static void cancel(String serviceName, String serviceInstanceId) {
        log.info("服务实例下线【" + serviceName + ", " + serviceInstanceId + "】");
    }
}
