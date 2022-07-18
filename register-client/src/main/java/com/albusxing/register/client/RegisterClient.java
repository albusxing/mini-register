package com.albusxing.register.client;
import cn.hutool.json.JSONUtil;
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
@Slf4j
public class RegisterClient {

    public static final String SERVICE_NAME = "msa-user";
    public static final String IP = "192.168.10.108";
    public static final String HOSTNAME = "user01";
    public static final int PORT = 9572;
    private static final Long HEARTBEAT_INTERVAL = 30 * 1000L;


    /**
     * 服务实例id
     */
    private final String serviceInstanceId;
    /**
     * 心跳线程
     */
    private HeartbeatWorker heartbeatWorker;
    /**
     * 心跳线程运行状态
     * 客户端调用shutdown方法更改isRunning状态；心跳线程读取isRunning状态，不断地发送心跳；
     * 这2个线程在读写这个共享变量，所以需要使用volatile保证变量的可见性
     */
    private volatile Boolean isRunning;

    /**
     * 客户端缓存的注册表
     */
    private ClientCachedRegistry registry;

    public RegisterClient() {
        this.serviceInstanceId = UUID.randomUUID().toString();
        this.heartbeatWorker = new HeartbeatWorker();
        this.isRunning = Boolean.TRUE;
        this.registry = new ClientCachedRegistry(this);
    }

    /**
     * 客户端启动方法
     */
    public void bootstrap() {
        try {
            // 创建服务注册线程
            RegisterWorker registerWorker = new RegisterWorker();
            registerWorker.start();
            // join(): main线程会等待注册线程执行完成之后，才开始执行心跳线程
            registerWorker.join();

            // 创建心跳续约线程
            heartbeatWorker.start();

            // 初始化客户端缓存组件
            registry.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 客户端关闭
     */
    public void shutdown() {
        this.isRunning = Boolean.FALSE;
        // 打断心跳线程
        // 关闭的时候，如果还有心跳线程处于sleep中，就打断线程的睡眠
        this.heartbeatWorker.interrupt();
        this.registry.destroy();
    }


    /**
     * 客户端是否在运行
     * @return
     */
    public boolean isRunning() {
        return isRunning;
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
            while (isRunning) {
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
