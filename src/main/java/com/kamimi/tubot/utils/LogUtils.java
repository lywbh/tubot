package com.kamimi.tubot.utils;

import net.mamoe.mirai.utils.MiraiLogger;

public class LogUtils {

    private static MiraiLogger logger;

    public static void init(MiraiLogger logger) {
        LogUtils.logger = logger;
    }

    public static MiraiLogger getLogger() {
        return logger;
    }

}
