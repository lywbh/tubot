package com.kamimi.tubot.config;

import java.net.InetSocketAddress;
import java.net.ProxySelector;

/**
 * 代理配置
 */
public class ProxyConfig {

    /**
     * 自定义HTTP代理
     */
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 10809;
    public static final ProxySelector HTTP_PROXY = ProxySelector.of(new InetSocketAddress(HOST, PORT));

    /**
     * 系统代理
     */
    public static final ProxySelector DEFAULT_PROXY = ProxySelector.getDefault();

}
