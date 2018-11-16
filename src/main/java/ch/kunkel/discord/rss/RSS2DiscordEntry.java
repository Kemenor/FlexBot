package ch.kunkel.discord.rss;

public class RSS2DiscordEntry {
	private String webhookURL;
	private String rssURL;
	private String lastTitle;

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

}
