package ch.kunkel.discord.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface Command extends Runnable {
	/**
	 * creates a new instance that will be executed
	 * 
	 * @param args  the command that was typed in discord, first index is always
	 *              "!flex" second is the commands name
	 * @param event the MessageEvent that generated this command
	 * @return a new instance of this command
	 */
	public Command newInstance(String[] args, GuildMessageReceivedEvent event);

	public String getHelpString();
}
