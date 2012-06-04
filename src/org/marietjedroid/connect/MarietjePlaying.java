package org.marietjedroid.connect;

public class MarietjePlaying extends MarietjeTrack {
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
