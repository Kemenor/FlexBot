package ch.kunkel.discord.command;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ch.kunkel.discord.command.management.CreateCommand;
import ch.kunkel.discord.command.management.DisconnectCommand;
import ch.kunkel.discord.command.management.HelpCommand;
import ch.kunkel.discord.command.management.JoinCommand;
import ch.kunkel.discord.command.music.PauseCommand;
import ch.kunkel.discord.command.music.PlayCommand;
import ch.kunkel.discord.command.music.PlaylistCommand;
import ch.kunkel.discord.command.music.SearchCommand;
import ch.kunkel.discord.command.music.SetVolumeCommand;
import ch.kunkel.discord.command.music.ShuffleCommand;
import ch.kunkel.discord.command.music.SkipCommand;
import ch.kunkel.discord.command.music.StopCommand;
import ch.kunkel.discord.command.music.UnpauseCommand;
import ch.kunkel.discord.command.rss.RegisterCommand;
import ch.kunkel.discord.command.rss.UnRegisterCommand;

public class CommandModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandFactory.class);
		Multibinder<Command> mapbinder = Multibinder.newSetBinder(binder(), Command.class);
		mapbinder.addBinding().to(CreateCommand.class);
		mapbinder.addBinding().to(HelpCommand.class);
		mapbinder.addBinding().to(JoinCommand.class);
		mapbinder.addBinding().to(DisconnectCommand.class);
		mapbinder.addBinding().to(PlayCommand.class);
		mapbinder.addBinding().to(StopCommand.class);
		mapbinder.addBinding().to(SearchCommand.class);
		mapbinder.addBinding().to(PauseCommand.class);
		mapbinder.addBinding().to(UnpauseCommand.class);
		mapbinder.addBinding().to(SkipCommand.class);
		mapbinder.addBinding().to(SetVolumeCommand.class);
		mapbinder.addBinding().to(ShuffleCommand.class);
		mapbinder.addBinding().to(PlaylistCommand.class);
		mapbinder.addBinding().to(RegisterCommand.class);
		mapbinder.addBinding().to(UnRegisterCommand.class);

	}

}
