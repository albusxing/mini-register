package com.albusxing.register.client;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.albusxing.register.core.HeartbeatRequest;
import com.albusxing.register.core.HeartbeatResponse;
import com.albusxing.register.core.RegisterRequest;
import com.albusxing.register.core.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
import java.util.UUID;

/**
 * 负责与register-server通信
 * @author liguoqing
 */
public class RegisterClient {

    public static final String SERVICE_NAME = "inventory-service";
    public static final String IP = "192.168.31.207";
    public static final String HOSTNAME = "inventory01";
    public static final int PORT = 9000;
    private static final Long HEARTBEAT_INTERVAL = 30 * 1000L;

    private static final Log log = LogFactory.get();

    /**
     * 服务实例id
     */
    private final String serviceInstanceId;

    public RegisterClient() {
        this.serviceInstanceId = UUID.randomUUID().toString();
    }

    public void start() {
        try {
            // 创建服务注册线程
            RegisterWorker registerWorker = new RegisterWorker();
            registerWorker.start();
            // main线程会等待注册线程执行完成之后，才开始执行心跳线程
            registerWorker.join();

            // 创建心跳续约线程
            HeartbeatWorker heartbeatWorker = new HeartbeatWorker();
            heartbeatWorker.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 服务注册线程
     */
    private class RegisterWorker extends Thread {

        @Override
        public void run() {
            // 获取当前服务的信息，包括ip地址、端口、主机名
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setServiceName(SERVICE_NAME);
            registerRequest.setServiceInstanceId(serviceInstanceId);
            registerRequest.setHostname(HOSTNAME);
            registerRequest.setIp(IP);
            registerRequest.setPort(PORT);
            JSONUtil.toJsonStr(registerRequest);
            log.info("服务实例注册：{}", JSONUtil.toJsonStr(registerRequest));
            RegisterResponse response = HttpSender.register(registerRequest);
            log.info("服务实例注册结果：{}", response.getStatus());
        }
    }

    /**
     * 心跳线程
     */
    private class HeartbeatWorker extends Thread {

        @Override
        public void run() {
            HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            heartbeatRequest.setServiceInstanceId(serviceInstanceId);
            heartbeatRequest.setServiceName(SERVICE_NAME);
            while (true) {
                try {
                    log.info("发送服务心跳：{}", JSONUtil.toJsonStr(heartbeatRequest));
                    HeartbeatResponse heartbeatResponse = HttpSender.heartbeat(heartbeatRequest);
                    log.info("发送服务心跳结果：{}", heartbeatResponse.getStatus());
                    if (Objects.equals(heartbeatResponse.getStatus(), RegisterResponse.SUCCESS)) {
                        // 休眠30s
                        Thread.sleep(HEARTBEAT_INTERVAL);
                    }
                } catch (Exception e) {
                    log.info("发送服务心跳出现异常", e);
                    e.printStackTrace();
                }

            }
        }
    }
}
