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
import javax.xml.bind.annotation.XmlElement;
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
		logger.debug("created RSS Manager");
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
		logger.debug("adding RSS with webhook");
		save.saveList.add(new RSS2DiscordEntry(webhookURL, rssUrl, ""));
		update();
	}

	/**
	 * removed by webhook url
	 * 
	 * @param url
	 */
	public void removeRSSFeed(String url) {
		logger.debug("removed RSSfeed with webhook");
		save.saveList = save.saveList.stream()
				.filter((RSS2DiscordEntry entry) -> entry.getWebhookURL().equals(url))
				.collect(Collectors.toList());
		update();
	}

	public void update() {
		logger.debug("reading rss feeds");
		Remark remark = new Remark();
		for (RSS2DiscordEntry rss2DiscordEntry : save.saveList) {
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
						String content = item.getElementsByTagName("description").item(0).getTextContent();
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
			RSS2DiscordSave save =  (RSS2DiscordSave) jaxbUnmarshaller.unmarshal(rssFile);
			logger.debug("loaded {} rss entries", save.saveList.size());
			return save;
		} catch (JAXBException e) {
			logger.warn("Couldn't load registered loosers.");
		}
		return new RSS2DiscordSave();
	}

	private void save(RSS2DiscordSave save, File saveFile) {
		logger.debug("saving rss {} ", save.saveList.size());
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(RSS2DiscordSave.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.marshal(save, saveFile);
		} catch (JAXBException e) {
			logger.warn("Couldn't save registered loosers.", e);
		}
	}


	
}
