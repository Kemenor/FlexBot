package ch.kunkel.discord.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    public void execute(String[] args, MessageReceivedEvent event);
}
