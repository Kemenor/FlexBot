package ch.kunkel.discord.voice;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.frontend.DataProcessingException;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.CombinedAudio;
import net.dv8tion.jda.core.audio.UserAudio;

public class VoiceManager implements AudioReceiveHandler, AutoCloseable {
	private Logger logger = LoggerFactory.getLogger(VoiceManager.class);
	private PipedOutputStream pop = new PipedOutputStream();
	private PipedInputStream pip = new PipedInputStream();

	private AudioFormat discordFormat = OUTPUT_FORMAT;
	private AudioFormat sphinxFormat = new AudioFormat(16000.0f, 16, 1, true, false);
	private VoiceConsumer consumer;
	private AudioInputStream convertedStream;
	private AudioInputStream audioInput;
	private boolean closed = false;

	public VoiceManager(Configuration configuration) throws IOException {
		logger.debug("added voice manager");
		if (!AudioSystem.isConversionSupported(sphinxFormat, discordFormat)) {
			throw new IOException("Can't convert into voice recognition format!");
		}
		try {
			pop.connect(pip);
			audioInput = new AudioInputStream(pip, discordFormat, AudioSystem.NOT_SPECIFIED);
			convertedStream = AudioSystem.getAudioInputStream(sphinxFormat, audioInput);
		} catch (IOException e) {
			logger.error("Voice Recognition isn't working", e);
			throw e;
		}
		consumer = new VoiceConsumer(configuration);
		Thread t = new Thread(consumer);
		t.start();
	}

	@Override
	public boolean canReceiveCombined() {
		return true;
	}

	@Override
	public boolean canReceiveUser() {
		return false;
	}

	@Override
	public void handleCombinedAudio(CombinedAudio combinedAudio) {
//		if (closed || userAudio.getUser().isBot()) {
//			logger.debug("listening to own voice");
//			// We don't take kindly to my kind around here!
//			return;
//		}
		// this could be expanded so every user gets his own recognizer but whatever
		try {
			byte[] data = combinedAudio.getAudioData(1.0);
			pop.write(data);
		} catch (IOException e) {
			logger.error("Can't write data to voice recognition", e);
		}
	}

	@Override
	public void handleUserAudio(UserAudio userAudio) {
		
	}

	@Override
	public void close() throws IOException {
		closed = true;
	}

	class VoiceConsumer implements Runnable {
		private Configuration configuration;
		private StreamSpeechRecognizer recognizer;

		public VoiceConsumer(Configuration configuration) {
			this.configuration = configuration;
		}

		@Override
		public void run() {
			try {
				recognizer = new StreamSpeechRecognizer(configuration);
				recognizer.startRecognition(convertedStream);
				SpeechResult result;
				while (!closed && (result = recognizer.getResult()) != null) {
					// TODO check if command and add command creation
					System.out.format("Hypothesis: %s\n", result.getHypothesis());
				}
				recognizer.stopRecognition();
				convertedStream.close();
				audioInput.close();
				pop.close();
				pip.close();
			} catch (IOException | DataProcessingException e) {
				logger.error("Voice Recognition isn't working", e);
			}
		}

	}
}
