package com.kamimi.tubot.obj;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class CommandInfo {

    private String command;

    private List<String> params;

    private CommandInfo() {
        params = new LinkedList<>();
    }

    public static CommandInfo fromString(String str) {
        CommandInfo commandInfo = new CommandInfo();
        if (str != null) {
            List<String> strSplit = Arrays.asList(str.split(" "));
            commandInfo.setCommand(strSplit.get(0));
            if (strSplit.size() >= 2) {
                for (int i = 1; i < strSplit.size(); ++i) {
                    if (!strSplit.get(i).isEmpty()) {
                        commandInfo.params.add(strSplit.get(i));
                    }
                }
            }
        }
        return commandInfo;
    }
}
