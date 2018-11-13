package ch.kunkel.discord.command;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.Config;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CreateCommand implements Command {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String[] args;
	private MessageReceivedEvent event;
	private Config config = Config.getInstance();

	protected CreateCommand() {
	}

	private CreateCommand(String[] args, MessageReceivedEvent event) {
		this.args = args;
		this.event = event;
	}

	@Override
	public void run() {
		logger.debug("create {}", Arrays.toString(args));
		String category = null;
		if (args.length >= 3) {
			StringBuilder catName = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				catName.append(args[i]);
				catName.append(" ");
			}
			category = catName.toString().trim();
		}
		logger.debug("running create command with category {}", category);
		Guild guild = event.getGuild();

		if (category != null) {
			List<Category> categoriesByName = guild.getCategoriesByName(category, true);
			if (!categoriesByName.isEmpty()) {
				logger.debug("creating category voice channel");
				categoriesByName.get(0).createVoiceChannel(config.getProperty("Channel.Manager")).complete();
				event.getChannel().sendMessage("Created channel \"" + config.getProperty("Channel.Manager")
						+ "\" at category \"" + categoriesByName.get(0).getName() + "\".").complete();
				return;
			}
		}
		logger.debug("creating guild voice channel");
		guild.getController().createVoiceChannel(config.getProperty("Channel.Manager")).complete();
		event.getChannel().sendMessage("Created channel " + config.getProperty("Channel.Manager") + " at topplevel.")
				.complete();
	}

	@Override
	public Command newInstance(String[] args, MessageReceivedEvent event) {
		return new CreateCommand(args, event);
	}

	@Override
	public String getHelpString() {
		return "creates the flex voice channel if none exists. Usage: create <category>";
	}

}