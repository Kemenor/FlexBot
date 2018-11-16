package ch.kunkel.discord.rss;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RSS2DiscordSave {
	@XmlElement(name = "entry")
	List<RSS2DiscordEntry> saveList = new ArrayList<>();
}