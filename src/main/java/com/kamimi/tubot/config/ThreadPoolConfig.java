package com.kamimi.tubot.config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池配置
 */
public class ThreadPoolConfig {

    public static final ScheduledExecutorService SCHEDULED_POOL = new ScheduledThreadPoolExecutor(16, r -> new Thread(r, "SCHEDULED_POOL"));

}
