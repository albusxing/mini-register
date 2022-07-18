package com.albusxing.register.server;

/**
 * @author Albusxing
 * @created 2022/7/18
 */
public class HeartbeatMeasureRate {

    /**
     * 最近一分钟的心跳次数
     */
    private long latestMinuteHeartbeatRate = 0L;
    /**
     * 最近一分钟的时间戳
     */
    private long latestMinuteTimestamp = System.currentTimeMillis();

    private static final HeartbeatMeasureRate INSTANCE = new HeartbeatMeasureRate();

    private HeartbeatMeasureRate() {

    }

    public static HeartbeatMeasureRate getInstance() {
        return INSTANCE;
    }


    /**
     * 增加最近一分钟的心跳次数
     */
    public void increment() {

        long currentTime = System.currentTimeMillis();
        if (currentTime - latestMinuteTimestamp > 60 * 1000) {
            latestMinuteTimestamp = System.currentTimeMillis();
            latestMinuteHeartbeatRate = 0L;
        }
        latestMinuteHeartbeatRate++;
    }

    /**
     * 获取最近一分钟的心跳次数
     * @return
     */
    public long get() {
        return latestMinuteHeartbeatRate;
    }

}
