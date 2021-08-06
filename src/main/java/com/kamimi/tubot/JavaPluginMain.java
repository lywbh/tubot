package com.kamimi.tubot;

import com.kamimi.tubot.config.ThreadPoolConfig;
import com.kamimi.tubot.module.LoliconModule;
import com.kamimi.tubot.module.SaucenaoModule;
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
import net.mamoe.mirai.message.data.*;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.HashBasedTable;

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
        // 监听群消息
        subscribeGroup();
    }

    private static final HashBasedTable<Long, Long, String> commandHolder = HashBasedTable.create();

    private void subscribeGroup() {
        GlobalEventChannel.INSTANCE.parentScope(this).subscribeAlways(GroupMessageEvent.class, g -> {
            String lastCommand = commandHolder.get(g.getGroup().getId(), g.getSender().getId());
            if (lastCommand != null) {
                // 间接命令
                commandHolder.remove(g.getGroup().getId(), g.getSender().getId());
                switch (lastCommand) {
                    case "!source":
                        for (SingleMessage element : g.getMessage()) {
                            if (element instanceof Image) {
                                String imgUrl = Image.queryUrl((Image) element);
                                LogUtils.getLogger().info("imgUrl:" + imgUrl);
                                Map<String, String> bestMatch = SaucenaoModule.bestMatch(imgUrl);
                                if (!bestMatch.isEmpty()) {
                                    StringBuilder sb = new StringBuilder();
                                    bestMatch.forEach((k, v) -> sb.append(k).append(" --> ").append(v).append("\n"));
                                    g.getGroup().sendMessage(sb.toString());
                                } else {
                                    g.getGroup().sendMessage("没有找到出处哟");
                                }
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
                // 直接命令
                CommandInfo commandInfo = CommandInfo.fromString(g.getMessage().contentToString());
                switch (commandInfo.getCommand()) {
                    case "!source":
                        commandHolder.put(g.getGroup().getId(), g.getSender().getId(), "!source");
                        MessageChain messageChain = new MessageChainBuilder()
                                .append(new At(g.getSender().getId()))
                                .append(" 请发送要查询的图片")
                                .build();
                        g.getGroup().sendMessage(messageChain);
                        break;
                    case "!push":
                        boolean isOn;
                        long gapMinutes;
                        if (commandInfo.getParams().isEmpty()) {
                            g.getGroup().sendMessage("命令有误，请检查");
                            return;
                        } else {
                            if ("on".equals(commandInfo.getParams().get(0))) {
                                isOn = true;
                            } else if ("off".equals(commandInfo.getParams().get(0))) {
                                isOn = false;
                            } else {
                                g.getGroup().sendMessage("命令有误，请检查");
                                return;
                            }
                            if (commandInfo.getParams().size() == 1) {
                                gapMinutes = 30;
                            } else {
                                try {
                                    gapMinutes = Long.parseLong(commandInfo.getParams().get(1));
                                } catch (NumberFormatException e) {
                                    g.getGroup().sendMessage("命令有误，请检查");
                                    return;
                                }
                            }
                        }
                        String responseMsg;
                        if (isOn) {
                            if (setPushTask(g.getBot(), g.getGroup(), gapMinutes)) {
                                responseMsg = "群推送已开启，推送间隔" + gapMinutes + "min";
                            } else {
                                responseMsg = "推送已经在开启状态了";
                            }
                        } else {
                            if (delPushTask(g.getBot(), g.getGroup())) {
                                responseMsg = "群推送已关闭";
                            } else {
                                responseMsg = "还没开启推送哦，您是要开启推送吗？请使用!push on命令";
                            }
                        }
                        g.getGroup().sendMessage(responseMsg);
                    default:
                        break;
                }
            }
        });
    }

    private static final HashBasedTable<Long, Long, ScheduledFuture<?>> taskHolder = HashBasedTable.create();

    private boolean setPushTask(Bot bot, Group group, long gapMinutes) {
        if (taskHolder.contains(bot.getId(), group.getId())) {
            return false;
        }
        ScheduledFuture<?> task = ThreadPoolConfig.SCHEDULED_POOL.scheduleAtFixedRate(() -> {
            String sUrl = LoliconModule.randomPic(LoliconModule.Rating.MIX);
            InputStream sStream = HttpUtils.getStream(sUrl);
            String qUrl = LoliconModule.randomPic(LoliconModule.Rating.R18);
            InputStream qStream = HttpUtils.getStream(qUrl);
            Contact.sendImage(group, sStream);
            Contact.sendImage(group, qStream);
        }, gapMinutes, gapMinutes, TimeUnit.MINUTES);
        taskHolder.put(bot.getId(), group.getId(), task);
        return true;
    }

    private boolean delPushTask(Bot bot, Group group) {
        ScheduledFuture<?> task = taskHolder.get(bot.getId(), group.getId());
        if (task == null) {
            return false;
        }
        task.cancel(false);
        taskHolder.remove(bot.getId(), group.getId());
        return true;
    }

}
