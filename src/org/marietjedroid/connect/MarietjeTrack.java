package org.marietjedroid.connect;

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

	/**
	 * @return the byKey
	 */
	public String getByKey() {
		return byKey;
	}

	/**
	 * Requester, may be null (in that case it was auto-queued) 
	 */
	private String byKey;
}
