package com.albusxing.register.server;

import java.util.Map;

/**
 * 服务存活状态监控器
 * @author Albusxing
 * @created 2022/6/26
 */
public class ServiceAliveMonitor {

    /**
     * 检查服务实例是否存活的间隔
     */
    private static final Long CHECK_ALIVE_INTERVAL = 60 * 1000L;

    /**
     * 负责监控服务的健康状态
     */
    private Daemon daemon;


    public ServiceAliveMonitor() {
        Daemon daemon = new Daemon();
        // 设置线程为Daemon线程
        daemon.setDaemon(true);
        this.daemon = daemon;
    }


    /**
     * 负责启动后台线程
     */
    public void start() {
        daemon.start();
    }

    /**
     * 监控服务健康状态的后台线程
     */
    private class Daemon extends Thread {

        private Registry registry = Registry.getInstance();

        @Override
        public void run() {
            while (true) {
                try {
                    Map<String, Map<String, ServiceInstance>> registryMap = this.registry.getRegistry();
                    for (Map.Entry<String, Map<String, ServiceInstance>> serviceEntry : registryMap.entrySet()) {
                        String serviceName = serviceEntry.getKey();
                        Map<String, ServiceInstance> serviceInstanceMap = serviceEntry.getValue();
                        for (Map.Entry<String, ServiceInstance> instanceEntry : serviceInstanceMap.entrySet()) {
                            ServiceInstance serviceInstance = instanceEntry.getValue();
                            if (!serviceInstance.isAlive()) {
                                registry.removeServiceInstance(serviceName, serviceInstance.getServiceInstanceId());
                            }
                        }
                    }

                    Thread.sleep(CHECK_ALIVE_INTERVAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
