package ch.kunkel.discord.rss;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.overzealous.remark.Remark;

import ch.kunkel.discord.Config;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;

public class RSSManager {
	private Logger logger = LoggerFactory.getLogger(RSSManager.class);
	private RSS2DiscordSave save = new RSS2DiscordSave();
	private Config config = Config.getInstance();
	private Timer rssUpdate = new Timer();
	private static RSSManager instance = new RSSManager();
	private final File rssFile;

	private RSSManager() {
		rssFile = new File(config.getProperty("RSS.saveFile", "rssfeeds.xml"));
		int interval = config.getProperty("RSS.updateInterval", 300);
		save = load(rssFile);
		rssUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 0, interval * 1000);
	}

	public static RSSManager getInstance() {
		return instance;
	}

	public void addRSSFeed(String rssUrl, String webhookURL) {
		save.list.add(new RSS2DiscordEntry(webhookURL, rssUrl, ""));
	}

	/**
	 * removed by rssurl or by webhook url
	 * 
	 * @param url
	 */
	public void removeRSSFeed(String url) {
		save.list = save.list.stream()
				.filter((RSS2DiscordEntry entry) -> entry.getRssURL().equals(url) || entry.getWebhookURL().equals(url))
				.collect(Collectors.toList());
	}

	public void update() {
		Remark remark = new Remark();
		for (RSS2DiscordEntry rss2DiscordEntry : save.list) {
			try {
				// Download RSS Feed
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = db.parse(new URL(rss2DiscordEntry.getRssURL()).openStream());
				NodeList items = doc.getElementsByTagName("item");

				Element item = (Element) items.item(0);
				if (item != null) {
					String title = item.getElementsByTagName("title").item(0).getTextContent();
					if (!title.equals(rss2DiscordEntry.getLastTitle())) {
						// new item in channel
						rss2DiscordEntry.setLastTitle(title);
						String content = item.getElementsByTagName("title").item(0).getTextContent();
						WebhookClientBuilder builder = new WebhookClientBuilder(rss2DiscordEntry.getWebhookURL());
						try (WebhookClient client = builder.build()) {
							client.send(remark.convert(content));
						}
					}
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				logger.warn("Couldn't read rss feed under {}", rss2DiscordEntry.getRssURL(), e);
			}
		}

		save(save, rssFile);
	}

	private RSS2DiscordSave load(File rssFile) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(RSS2DiscordSave.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (RSS2DiscordSave) jaxbUnmarshaller.unmarshal(rssFile);
		} catch (JAXBException e) {
			logger.warn("Couldn't load registered loosers.");
		}
		return new RSS2DiscordSave();
	}

	private void save(RSS2DiscordSave save, File saveFile) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(RSS2DiscordSave.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(save, saveFile);
		} catch (JAXBException e) {
			logger.warn("Couldn't save registered loosers.");
		}
	}

	@XmlRootElement
	class RSS2DiscordSave {
		List<RSS2DiscordEntry> list = new ArrayList<>();
	}
}
