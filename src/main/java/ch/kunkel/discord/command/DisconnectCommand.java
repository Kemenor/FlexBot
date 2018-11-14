package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class DisconnectCommand implements Command {

	private GuildMessageReceivedEvent event;

	public DisconnectCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	protected DisconnectCommand() {
	}

	@Override
	public void run() {
		MusicManager.getInstance().disconnect(event.getGuild());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new DisconnectCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Disconnects the bot from his conencted voice channel";
	}

}
