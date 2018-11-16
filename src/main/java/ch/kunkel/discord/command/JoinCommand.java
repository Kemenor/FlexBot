package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class JoinCommand implements Command {

	private GuildMessageReceivedEvent event;

	protected JoinCommand() {
	}

	private JoinCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		VoiceChannel vc = event.getMember().getVoiceState().getChannel();
		if (vc == null) {
			event.getChannel().sendMessage("It seems you are not connected to a voice channel!").queue();
			return;
		}
		// manager is shared
		MusicManager.getInstance().join(vc);
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new JoinCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Joins the users voice channel";
	}

	@Override
	public int getMaxArgsCount() {
		return 0;
	}
}
