package ch.kunkel.discord.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RSS2DiscordEntry {
	private String webhookURL;
	private String rssURL;
	private String lastTitle;

	private RSS2DiscordEntry() {
	}
	
	public RSS2DiscordEntry(String webhookURL, String rssURL, String lastTitle) {
		this.webhookURL = webhookURL;
		this.rssURL = rssURL;
		this.lastTitle = lastTitle;
	}

	public String getWebhookURL() {
		return webhookURL;
	}

	public String getRssURL() {
		return rssURL;
	}

	public String getLastTitle() {
		return lastTitle;
	}

	public void setLastTitle(String lastTitle) {
		this.lastTitle = lastTitle;
	}

	public void setWebhookURL(String webhookURL) {
		this.webhookURL = webhookURL;
	}

	public void setRssURL(String rssURL) {
		this.rssURL = rssURL;
	}
	
	

}
