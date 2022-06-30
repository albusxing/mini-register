package com.albusxing.register.client;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.albusxing.register.core.HeartbeatRequest;
import com.albusxing.register.core.HeartbeatResponse;
import com.albusxing.register.core.RegisterRequest;
import com.albusxing.register.core.RegisterResponse;
import java.util.Objects;

/**
 * 负责向register-server注册服务的信息
 * @author liguoqing
 */
public class RegisterClientWorker extends Thread {

    public static final String SERVICE_NAME = "inventory-service";
    public static final String IP = "192.168.31.207";
    public static final String HOSTNAME = "inventory01";
    public static final int PORT = 9000;

    private static final Log log = LogFactory.get();

    /**
     * 是否完成注册
     */
    private Boolean finishedRegister;
    /**
     * 服务实例id
     */
    private String serviceInstanceId;


    public RegisterClientWorker(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
        finishedRegister = false;
    }


    @Override
    public void run() {
        if (!finishedRegister) {
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
            if (Objects.equals(response.getStatus(), RegisterResponse.SUCCESS)) {
                finishedRegister = true;
            }
        }


        if (finishedRegister) {
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
                        Thread.sleep(30 * 1000);
                    }
                } catch (Exception e) {
                    log.info("发送服务心跳出现异常", e);
                    e.printStackTrace();
                }

            }
        }
    }
}
