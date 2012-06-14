package org.marietjedroid.connect;

import org.json.JSONObject;

public class MarietjePlaying extends MarietjeTrack {
	/**
	 * @param byKey The one who requested it
	 * @param servertime 
	 * @param endtime
	 * @param media media dict
	 */
	public MarietjePlaying(String byKey, double servertime, double endtime, JSONObject media ) {
		super(media);
		// TODO Auto-generated constructor stub
	}
	
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
