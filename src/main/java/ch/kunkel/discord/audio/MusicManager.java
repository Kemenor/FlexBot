package ch.kunkel.discord.audio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicManager {
	private static MusicManager instance = new MusicManager();
	private AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	private Map<Guild, TrackScheduler> map = new HashMap<>();
	private Logger logger = LoggerFactory.getLogger(MusicManager.class);

	public MusicManager() {
		playerManager.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		AudioSourceManagers.registerRemoteSources(playerManager);

	}

	public void join(VoiceChannel vc) {
		if (map.containsKey(vc.getGuild())) {
			logger.debug("already connected switching channel.");
			AudioManager am = vc.getGuild().getAudioManager();
			am.openAudioConnection(vc);
		} else {
			logger.debug("not connected to guild creating guild connection");
			TrackScheduler trackScheduler = new TrackScheduler(playerManager);

			AudioManager am = vc.getGuild().getAudioManager();
			am.openAudioConnection(vc);
			am.setSendingHandler(new AudioPlayerSendHandler(trackScheduler));
			am.openAudioConnection(vc);
			map.put(vc.getGuild(), trackScheduler);
			logger.debug("Guild id: {}", vc.getGuild().getId());
		}
	}

	public void shuffle(Guild guild) {
		if (map.containsKey(guild)) {
			map.get(guild).shuffle();
		}
	}

	public void disconnect(Guild guild) {
		AudioManager am = guild.getAudioManager();
		am.setSendingHandler(null);
		am.setReceivingHandler(null);
		am.closeAudioConnection();
		map.remove(guild);
	}

	public void play(String identifier, Guild guild, TextChannel text) {
		if (map.containsKey(guild)) {
			if ("".equals(identifier)) {
				map.get(guild).start();
			} else {
				logger.debug("trying to find {}", identifier);
				playerManager.loadItem(identifier, new AudioLoadResultHandler() {
					@Override
					public void trackLoaded(AudioTrack track) {
						map.get(guild).queue(track);
						text.sendMessage("Added a song with title \"" + track.getInfo().title + "\"").queue();
					}

					@Override
					public void playlistLoaded(AudioPlaylist playlist) {
						if (identifier.startsWith("ytsearch:")) {
							AudioTrack track = playlist.getTracks().get(0);
							map.get(guild).queue(track);

							text.sendMessage("Added a search result with title \"" + track.getInfo().title + "\"")
									.queue();
						} else {
							for (AudioTrack audioTrack : playlist.getTracks()) {
								map.get(guild).queue(audioTrack);
							}
							text.sendMessage("Added a playlist with title \"" + playlist.getName() + "\"").queue();
						}

					}

					@Override
					public void noMatches() {
						text.sendMessage("Couldn't find a song with identifier \"" + identifier + "\"").queue();
					}

					@Override
					public void loadFailed(FriendlyException exception) {
						text.sendMessage("Couldn't load song with \"" + identifier + "\" " + exception.getMessage())
								.queue();
					}
				});
			}
		} else {
			text.sendMessage("It seems I'm not connected to your Server!");
		}
	}

	public void stop(Guild guild, TextChannel text) {
		if (map.containsKey(guild)) {
			map.get(guild).clear();

		}
	}

	public void pause(Guild guild, TextChannel text) {
		if (map.containsKey(guild)) {
			map.get(guild).pause();
		}
	}

	public void unpause(Guild guild, TextChannel text) {
		if (map.containsKey(guild)) {
			map.get(guild).unpause();
		}
	}

	public void setVolume(Guild guild, TextChannel text, int volume) {
		if (map.containsKey(guild)) {
			map.get(guild).setVolume(volume);
		}
	}

	public void skip(Guild guild, TextChannel text) {
		if (map.containsKey(guild)) {
			map.get(guild).skip();
		}
	}

	public void clear(Guild guild, TextChannel text) {
		if (map.containsKey(guild)) {
			map.get(guild).clear();
		}
	}

	public static MusicManager getInstance() {
		return instance;
	}
}
