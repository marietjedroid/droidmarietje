package org.marietjedroid.connect;

import java.util.concurrent.Semaphore;

public class MarietjeClient {
	/**
	 * Locks until we have recieved tracks
	 */
	private final Semaphore tracksRetrieved = new Semaphore(0);
	/**
	 * Locks until we have retrieved a now playing track
	 */
	private final Semaphore playingRetrieved = new Semaphore(0);
	/**
	 * Blocks until we have retrieved requests
	 */
	private final Semaphore requestsRetrieved = new Semaphore(0);
	
	Semaphore getTracksRetrievedSemaphore() {
		return tracksRetrieved;
	}

	/**
	 * @return the playingRetrieved
	 */
	public Semaphore getPlayingRetrievedSemaphore() {
		return playingRetrieved;
	}

	public Semaphore getRequestsRetrievedSemaphore() {
		return requestsRetrieved;
	}
}
