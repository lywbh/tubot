package com.kamimi.tubot;

import com.kamimi.tubot.module.KonachanModule;
import com.kamimi.tubot.utils.HttpUtils;
import com.kamimi.tubot.utils.LogUtils;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.InputStream;

/**
 * 插件主类
 */
public final class JavaPluginMain extends JavaPlugin {

    public static final JavaPluginMain INSTANCE = new JavaPluginMain();

    private JavaPluginMain() {
        super(new JvmPluginDescriptionBuilder("org.kamimi.tubot", "0.1.0").build());
    }

    @Override
    public void onEnable() {
        // 初始化日志
        LogUtils.init(getLogger());
        // 初始化模块
        KonachanModule konachanModule = new KonachanModule();
        // 监听群消息
        GlobalEventChannel.INSTANCE.parentScope(this).subscribeAlways(GroupMessageEvent.class, g -> {
            switch (g.getMessage().contentToString()) {
                case "！pic":
                case "!pic":
                    String imgUrl = konachanModule.randomPic();
                    InputStream imgStream = HttpUtils.getStream(imgUrl);
                    Contact.sendImage(g.getGroup(), imgStream);
                    break;
                default:
                    break;
            }
        });
    }

}
