package com.kamimi.tubot.utils;

import java.util.Random;

public class RandomUtils {

    private static final Random random = new Random();

    /**
     * 获得一个[0,max)之间的整数。
     */
    public static int getRandomInt(int max) {
        return getRandomInt(0, max);
    }

    /**
     * 获得一个[min,max)之间的整数。
     */
    public static int getRandomInt(int min, int max) {
        return Math.abs(random.nextInt()) % max + min;
    }

}
