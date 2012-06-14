package org.marietjedroid.connect;

import org.json.JSONObject;

public class MarietjeRequest extends MarietjeTrack {
	
	/**
	 * @param artist
	 * @param title
	 * @param length
	 * @param uploader
	 * @param key Request key
	 * @param requester
	 */
	public MarietjeRequest(String artist, String title, double length,
			String uploader, int requestKey, String requester) {
		super(artist, title, length, uploader, uploader);
		
		this.key = requestKey;
		this.requester = requester;
		// TODO Auto-generated constructor stub
	}
	
	public MarietjeRequest (String requester, int requestKey, JSONObject media){
		super(media);
		this.requester = requester;
		this.key = requestKey;
	}

	/**
	 * identifier in the queue 
	 */
	private final int key;
	
	/**
	 * Requester
	 */
	private final  String requester;

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @return the requester
	 */
	public String getRequester() {
		return requester;
	}
	
	
}
