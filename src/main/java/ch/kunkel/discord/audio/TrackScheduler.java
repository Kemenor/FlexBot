package ch.kunkel.discord.audio;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

public class TrackScheduler extends AudioEventAdapter {
	private Logger logger = LoggerFactory.getLogger(TrackScheduler.class);
	private LinkedList<AudioTrack> queue = new LinkedList<>();
	private AudioPlayer player;

	public TrackScheduler(AudioPlayerManager playerManager) {
		player = playerManager.createPlayer();
		player.addListener(this);
		player.setVolume(10);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			player.playTrack(queue.poll());
		}
	}

	public void queue(AudioTrack track) {
		logger.debug("Player adding track");
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public AudioFrame provide() {
		return player.provide();
	}


	public void start() {
		if (player.isPaused()) {
			player.setPaused(false);
		}
		if (player.startTrack(queue.peek(), true)) {
			queue.poll();
		}
	}

	public void pause() {
		logger.debug("Player pausing");
		player.setPaused(true);
	}

	public void unpause() {
		logger.debug("Player unpausing");
		player.setPaused(false);
	}

	public void setVolume(int volume) {
		logger.debug("Player setting volume");
		player.setVolume(volume);
	}

	public void skip() {
		player.startTrack(queue.poll(), false);
	}

	public void clear() {
		player.stopTrack();
		queue.clear();
	}

	public void shuffle() {
		Collections.shuffle(queue);
	}
}