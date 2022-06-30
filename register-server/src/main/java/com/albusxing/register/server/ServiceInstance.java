package com.albusxing.register.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务实例
 * 包括服务实例的所有信息，包括服务名称、实例id，ip地址、hostname、端口，
 * 同时还包括契约（Lease）信息
 * 一个服务可能存在多个实例
 * @author liguoqing
 */
@Data
public class ServiceInstance {

    /**
     * 服务不再存活的周期时长
     */
    private static final Long NOT_ALIVE_PERIOD = 90 * 1000L;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务实例id
     */
    private String serviceInstanceId;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 主机名
     */
    private String hostname;
    /**
     * 服务契约
     */
    private Lease lease;


    public ServiceInstance() {
        this.lease = new Lease();
    }

    /**
     * 服务实例续约
     */
    public void renew() {
        this.lease.renew();
    }

    /**
     * 服务实例是否存活
     * @return
     */
    public Boolean isAlive() {
        return this.lease.isAlive();
    }

    /**
     * 契约
     * 维护服务实例和注册中心之间的关联关系，
     * 让注册中心指定服务实例还存活
     */
    @Data
    @Slf4j
    private class Lease {

        /**
         * 最近一次的心跳时间
         */
        private Long latestHeartbeatTime = System.currentTimeMillis();

        /**
         * 续租
         * 客户端register-client 发送一次心跳到注册中心register-server
         * 就对 register-client 和 register-server 之间的契约进行一次更新
         * 表明 register-client 在存活着！
         */
        public void renew() {
            this.latestHeartbeatTime = System.currentTimeMillis();
            log.info("服务实例[{}]，进行续约：{}", serviceInstanceId, latestHeartbeatTime);
        }

        /**
         * 判断当前服务实例契约是否有效
         * @return
         */
        public Boolean isAlive() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - latestHeartbeatTime > NOT_ALIVE_PERIOD) {
                log.info("服务实例[{}]不再存活", serviceInstanceId);
                return false;
            }
            log.info("服务实例[{}]保持存活", serviceInstanceId);
            return true;
        }
    }

}
