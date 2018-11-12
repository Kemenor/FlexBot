package ch.kunkel.discord.command;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    Map<String, Command> commandMap = new HashMap<>();

    public CommandFactory() {
        commandMap.put("join", new JoinCommand());
    }

    public void createCommand(String name, String[] args){

    }
}
