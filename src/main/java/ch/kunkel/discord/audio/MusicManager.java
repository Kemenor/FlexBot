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
		logger.debug("registering remote sources");
		playerManager.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		AudioSourceManagers.registerRemoteSources(playerManager);
	}

	public void join(VoiceChannel vc) {
		if (map.containsKey(vc.getGuild())) {
			AudioManager am = vc.getGuild().getAudioManager();
			am.openAudioConnection(vc);
		} else {
			TrackScheduler trackScheduler = new TrackScheduler(playerManager);

			AudioManager am = vc.getGuild().getAudioManager();
			am.openAudioConnection(vc);
			am.setSendingHandler(new AudioPlayerSendHandler(trackScheduler));
			// am.setReceivingHandler(new VoiceManager());
			am.openAudioConnection(vc);
			map.put(vc.getGuild(), trackScheduler);
		}
	}

	public void disconnect(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
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
						text.sendMessage("Added a song with identifier \"" + identifier + "\"").queue();
					}

					@Override
					public void playlistLoaded(AudioPlaylist playlist) {
						for (AudioTrack audioTrack : playlist.getTracks()) {
							map.get(guild).queue(audioTrack);
						}
						text.sendMessage("Added a playlist with identifier \"" + identifier + "\"").queue();
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
			map.get(guild).stop();
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
