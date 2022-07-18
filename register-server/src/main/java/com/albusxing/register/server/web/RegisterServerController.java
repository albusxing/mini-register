package com.albusxing.register.server.web;

import com.albusxing.register.core.HeartbeatRequest;
import com.albusxing.register.core.HeartbeatResponse;
import com.albusxing.register.core.RegisterRequest;
import com.albusxing.register.core.RegisterResponse;
import com.albusxing.register.server.HeartbeatMeasureRate;
import com.albusxing.register.server.Registry;
import com.albusxing.register.core.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册中心服务接口
 * 提供服务注册和发送心跳功能
 * @author liguoqing
 */
@RestController
@Slf4j
public class RegisterServerController {

    private Registry registry = Registry.getInstance();

    /**
     * 服务注册
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            ServiceInstance serviceInstance = new ServiceInstance();
            serviceInstance.setServiceName(registerRequest.getServiceName());
            serviceInstance.setServiceInstanceId(registerRequest.getServiceInstanceId());
            serviceInstance.setHostname(registerRequest.getHostname());
            serviceInstance.setIp(registerRequest.getIp());
            serviceInstance.setPort(registerRequest.getPort());
            registry.register(serviceInstance);
            registerResponse.setStatus(RegisterResponse.SUCCESS);
        } catch (Exception e) {
            log.error("服务注册失败", e);
            registerResponse.setStatus(RegisterResponse.FAILURE);
        }
        return registerResponse;
    }


    /**
     * 服务发送心跳
     * @param heartbeatRequest
     * @return
     */
    @PostMapping("/heartbeat")
    public HeartbeatResponse heartbeat(@RequestBody HeartbeatRequest heartbeatRequest) {
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        try {
            ServiceInstance serviceInstance = registry.getServiceInstance(heartbeatRequest.getServiceName(),
                    heartbeatRequest.getServiceInstanceId());
            serviceInstance.renew();
            heartbeatResponse.setStatus(HeartbeatResponse.SUCCESS);

            HeartbeatMeasureRate heartbeatMeasureRate = HeartbeatMeasureRate.getInstance();
            heartbeatMeasureRate.increment();
        } catch (Exception e) {
            log.error("服务发送心跳失败", e);
            heartbeatResponse.setStatus(HeartbeatResponse.FAILURE);
        }
        return heartbeatResponse;
    }
}
