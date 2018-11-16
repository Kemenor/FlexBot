package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ShuffleCommand implements Command {

	private GuildMessageReceivedEvent event;

	public ShuffleCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}
	
	protected ShuffleCommand() {
	}

	@Override
	public void run() {
		MusicManager.getInstance().shuffle(event.getGuild());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new ShuffleCommand(event);
	}

	@Override
	public String getHelpString() {
		return "shuffles the current playlist";
	}

	@Override
	public int getMaxArgsCount() {
		return 0;
	}

}
