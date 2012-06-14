package org.marietjedroid.connect;

public class MarietjePlaying extends MarietjeTrack {
	public MarietjePlaying(String artist, String title, double length,
			String uploader, String requester) {
		super(artist, title, length, requester, uploader);
		this.requester = requester;
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Diegene die heeft aangevraagd
	 */
	private String requester;

	/**
	 * Tijd op de server ten tijde van zenden bericht 
	 */
	private double serverTime;

	/**
	 * Eindtijd track 
	 */
	private double endTime;

	/**
	 * @return the serverTime
	 */
	public double getServerTime() {
		return serverTime;
	}

	/**
	 * @return the endTime
	 */
	public double getEndTime() {
		return endTime;
	} 
}
