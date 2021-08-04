package com.kamimi.tubot;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * 插件主类
 */
public final class JavaPluginMain extends JavaPlugin {

    public static final JavaPluginMain INSTANCE = new JavaPluginMain();

    private static final JvmPluginDescription pluginDescription = new JvmPluginDescriptionBuilder("org.kamimi.tubot", "0.1.0").info("图伯特").build();

    private JavaPluginMain() {
        super(pluginDescription);
    }

    @Override
    public void onEnable() {
        EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.parentScope(this);
        //监听群消息
        eventChannel.subscribeAlways(GroupMessageEvent.class, g -> getLogger().info(g.getMessage().contentToString()));
        //监听好友消息
        eventChannel.subscribeAlways(FriendMessageEvent.class, f -> getLogger().info(f.getMessage().contentToString()));
    }

}
