package com.albusxing.register.client;

import com.albusxing.register.core.ServiceInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端缓存的服务注册表
 * @author Albusxing
 * @created 2022/7/18
 */
public class ClientCachedRegistry {


    /**
     * 服务注册表的拉取时间间隔
     */
    private static final Long REGISTRY_FETCH_INTERVAL = 30 * 1000L;

    /**
     * 客户端缓存的注册表
     */
    private Map<String, Map<String, ServiceInstance>> registry = new HashMap<>();

    /**
     * 定时拉取注册表的后台线程
     */
    private Daemon daemon;

    private RegisterClient registerClient;


    public ClientCachedRegistry(RegisterClient registerClient) {
        this.registerClient = registerClient;
        this.daemon = new Daemon();
    }


    /**
     * 初始化
     */
    public void init() {
        this.daemon.start();
    }

    /**
     * 销毁
     */
    public void destroy() {
        this.daemon.interrupt();
    }


    private class Daemon extends Thread {
        @Override
        public void run() {
            boolean running = registerClient.isRunning();
            while (running) {
                try {
                    registry = HttpSender.fetchServiceRegistry();
                    Thread.sleep(REGISTRY_FETCH_INTERVAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Map<String, Map<String, ServiceInstance>> getRegistry() {
        return registry;
    }
}
