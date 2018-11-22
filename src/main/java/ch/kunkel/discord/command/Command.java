package ch.kunkel.discord.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command implements Runnable {

	/**
	 * creates a new instance that will be executed
	 * 
	 * @param args  the command that was typed in discord, first index is always
	 *              "!flex" second is the commands name
	 * @param event the MessageEvent that generated this command
	 * @return a new instance of this command
	 */
	public abstract Command newInstance(String[] args, GuildMessageReceivedEvent event);

	@Override
	public String toString() {
		String format = String.format("**%s** %s%n\t**Usage:** `!flex %s`", getCommandWord(), getDescription(),
				getUsage());
		String[] aliases = getAlias();
		if (aliases != null && aliases.length > 0) {
			StringBuilder aliasString = new StringBuilder();
			for (String alias : aliases) {
				aliasString.append(alias).append(", ");
			}
			aliasString.setLength(aliasString.length() - 2);
			format += String.format("%n\t**Alias:** %s", aliasString.toString());

		}
		return format;

	}

	public abstract String getCommandWord();

	public abstract String getDescription();

	public String getUsage() {
		return getCommandWord();
	}

	public String[] getAlias() {
		return null;
	}

}
