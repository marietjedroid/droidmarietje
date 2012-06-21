package org.marietjedroid.droidmarietje;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class LastFmParser {
	private String location;

	private String artist;
	private String title;
	private String album;
	private String albumArt;

	private int length;

	public LastFmParser(String location) {
		this.location = location;
	}

	public void parse() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(location);
			doc.getDocumentElement().normalize();

			Log.i("Parsing... ", location);

			NodeList nList = doc.getElementsByTagName("track");

			Element eElement = (Element) nList.item(0); // first item
														// (<track></track>)

			this.title = getTagValue("name", eElement); // track name
			this.length = Integer.parseInt(getTagValue("duration", eElement));

			Element artistTag = (Element) (eElement.getElementsByTagName("artist")).item(0);
			this.artist = getTagValue("name", artistTag);

			Element albumTag = (Element) (eElement.getElementsByTagName("album")).item(0);
			this.album = getTagValue("title", albumTag);
			this.albumArt = getTagValue("image", albumTag);

		} catch (Exception e) {
		}
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public int getLength() {
		return length;
	}

	public String getAlbum() {
		return album;
	}

	public String getAlbumArt() {
		return albumArt;
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		Log.d("Node Value:", nValue.getNodeValue() + " ");

		return nValue.getNodeValue();
	}
}