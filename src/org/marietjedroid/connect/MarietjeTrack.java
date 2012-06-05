package org.marietjedroid.connect;

import android.util.Log;

public class MarietjeTrack {
	
	/**
	 * Track artist 
	 */
	private String artist;
	/** 
	 * Track titel
	 */
	private String title;
	/**
	 * Uploaded door
	 */
	private String uploadedBy;
	
	/**
	 * Lengte van de track in seconden 
	 */
	private double length;
	
	/**
	 * Track identifier
	 */
	private String trackKey;
	
	private double timeLeft;
	
	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}

	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @return the trackKey
	 */
	public String getTrackKey() {
		return trackKey;
	}
	
	public String getTrackStringLength(){
		return timeToString(this.length);
	}
	
	public String getTrackStringTimeLeft(){
		return timeToString(this.timeLeft);
	}

	private static String timeToString(double time){
		double seconds = time / 1000;
    	int minutes = (int)(seconds / 60);
    	int seconds_i = (int)(seconds % 60);
    	
    	return minutes + ":" + seconds_i;
		
	}
	
	/**
	 * @return the byKey
	 */
	public String getByKey() {
		return byKey;
	}
	
	public double getTimeLeft(){
		return this.timeLeft;
	}

	public void setInfo(String key, String titel, String artiest, double length){
		this.trackKey = key;
		this.title = titel;
		this.artist = artiest;
		this.length = length;
		this.timeLeft = length;
		
		Log.d("Info", this.trackKey + " " + this.title);
	}
	
	/**
	 * Requester, may be null (in that case it was auto-queued) 
	 */
	private String byKey;

	public void decreaseTime() {
		this.timeLeft = Math.max(timeLeft - 1000, 0);
		
	}

}
