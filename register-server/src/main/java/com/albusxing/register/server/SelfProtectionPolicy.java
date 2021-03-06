package com.albusxing.register.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 自我保护机制
 * @author Albusxing
 * @created 2022/7/18
 */
@Slf4j
@Data
public class SelfProtectionPolicy {

    /**
     * 期望的一个心跳的次数，如果你有10个服务实例，这个数值就是10 * 2 = 20
     */
    private long expectedHeartbeatRate = 0L;
    /**
     * 期望的心跳次数的阈值，10 * 2 * 0.85 = 17，每分钟至少得有17次心跳，才不用进入自我保护机制
     */
    private long expectedHeartbeatThreshold = 0L;

    private static SelfProtectionPolicy INSTANCE = new SelfProtectionPolicy();

    private SelfProtectionPolicy() {

    }

    public static SelfProtectionPolicy getInstance() {
        return INSTANCE;
    }

    public boolean enabledSelfProtection() {
        HeartbeatMeasureRate heartbeatMeasureRate = HeartbeatMeasureRate.getInstance();
        long latestMinuteHeartbeatRate = heartbeatMeasureRate.get();

        if(latestMinuteHeartbeatRate < this.expectedHeartbeatThreshold) {
            log.info("【自我保护机制开启】最近一分钟心跳次数=" + latestMinuteHeartbeatRate + ", 期望心跳次数=" + this.expectedHeartbeatThreshold);
            return true;
        }
        log.info("【自我保护机制未开启】最近一分钟心跳次数=" + latestMinuteHeartbeatRate + ", 期望心跳次数=" + this.expectedHeartbeatThreshold);
        return false;
    }
}
