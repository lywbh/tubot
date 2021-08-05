package com.kamimi.tubot;

import com.kamimi.tubot.config.PushConfig;
import com.kamimi.tubot.config.ThreadPoolConfig;
import com.kamimi.tubot.module.YandereModule;
import com.kamimi.tubot.obj.CommandInfo;
import com.kamimi.tubot.utils.HttpUtils;
import com.kamimi.tubot.utils.LogUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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
        YandereModule yandereModule = new YandereModule();
        // 监听群消息
        GlobalEventChannel.INSTANCE.parentScope(this).subscribeAlways(GroupMessageEvent.class, g -> {
            CommandInfo commandInfo = CommandInfo.fromString(g.getMessage().contentToString());
            switch (commandInfo.getCommand()) {
                case "！pic":
                case "!pic":
                    String imgUrl;
                    if (commandInfo.getParams().isEmpty()) {
                        imgUrl = yandereModule.randomPic();
                    } else if (commandInfo.getParams().size() == 1) {
                        YandereModule.Rating rating = yandereModule.rating(commandInfo.getParams().get(0));
                        imgUrl = yandereModule.randomPic(rating);
                    } else {
                        YandereModule.Rating rating = yandereModule.rating(commandInfo.getParams().get(0));
                        String tag = commandInfo.getParams().get(1);
                        imgUrl = yandereModule.randomPic(rating, tag);
                    }
                    InputStream imgStream = HttpUtils.getStream(imgUrl);
                    Contact.sendImage(g.getGroup(), imgStream);
                    break;
                default:
                    break;
            }
        });
        // 每十分钟推送s和q各一张
        Bot.getInstances().forEach(bot -> ThreadPoolConfig.SCHEDULED_POOL.scheduleAtFixedRate(() -> {
            String sUrl = yandereModule.randomPic(YandereModule.Rating.SAFE);
            InputStream sStream = HttpUtils.getStream(sUrl);
            String qUrl = yandereModule.randomPic(YandereModule.Rating.QUESTIONABLE);
            InputStream qStream = HttpUtils.getStream(qUrl);
            PushConfig.PUSH_GROUPS.forEach(groupId -> {
                Group group = bot.getGroup(groupId);
                if (group != null) {
                    Contact.sendImage(group, sStream);
                    Contact.sendImage(group, qStream);
                }
            });
        }, PushConfig.PUSH_PER_MINUTES, PushConfig.PUSH_PER_MINUTES, TimeUnit.MINUTES));
    }

}
