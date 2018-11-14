package ch.kunkel.discord.voice;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
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

	public VoiceManager(Configuration configuration) throws IOException {
		if (!AudioSystem.isConversionSupported(sphinxFormat, discordFormat)) {
			throw new IOException("Can't convert into voice recognition format!");
		}
		try {
			pop.connect(pip);
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
		return false;
	}

	@Override
	public boolean canReceiveUser() {
		return true;
	}

	@Override
	public void handleCombinedAudio(CombinedAudio combinedAudio) {
		throw new UnsupportedOperationException("This handler doesn't accept combined Audio!");
	}
//	// 48KHz 16bit stereo signed BigEndian PCM
//	AudioFormat discordFormat = new AudioFormat(48000.0f, 16, 2, true, true);
//	// RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 16000 Hz
//	AudioFormat sphinxFormat = new AudioFormat(16000.0f, 16, 1, true, false);
//	System.out.println(AudioSystem.isConversionSupported(sphinxFormat, discordFormat));

	@Override
	public void handleUserAudio(UserAudio userAudio) {
		// userAudio
		try {
			byte[] data = userAudio.getAudioData(1.0);
			// TODO need to convert the data
			pop.write(data);
			// JDA: 48KHz 16bit stereo signed BigEndian PCM
			// RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 16000 Hz

			// 48Khz; 16 bit; stereo; signed BigEndian; PCM
			// 16Khz; 16 bit; mono; little-Endian; PCM
		} catch (IOException e) {
			logger.error("Can't write data to voice recognition", e);
		}
	}

	@Override
	public void close() throws IOException {
		this.consumer.stop();
		this.pip.close();
		this.pop.close();
	}

	class VoiceConsumer implements Runnable {
		private boolean run = true;
		private Configuration configuration;

		public VoiceConsumer(Configuration configuration) {
			this.configuration = configuration;
		}

		@Override
		public void run() {
			try {
				StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
				recognizer.startRecognition(pip);
				SpeechResult result;
				while (run && (result = recognizer.getResult()) != null) {
					// TODO check if command and add command creation
					System.out.format("Hypothesis: %s\n", result.getHypothesis());
				}
				logger.error("Recognizer stopping");
				recognizer.stopRecognition();
			} catch (IOException e) {
				logger.error("Voice Recognition isn't working", e);
			}
		}

		public void stop() {
			this.run = false;
		}

	}
}
