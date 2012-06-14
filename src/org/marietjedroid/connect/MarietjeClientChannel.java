package org.marietjedroid.connect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thomwiggers.Jjoyce.base.JoyceChannel;
import org.thomwiggers.Jjoyce.base.JoyceHub;
import org.thomwiggers.Jjoyce.base.JoyceRelay;

class MarietjeClientChannel extends JoyceChannel {

	/**
	 * TODO determine kind of object
	 */
	private MarietjeClient server;

	/**
	 * Contains tracklist recieved from maried
	 * 
	 * TODO Determine kind of object and use of _partialMedia in python version
	 */
	private JSONArray[] partialMedia;

	/**
	 * Temporary arraylist to store the portions of the media_part requests
	 */
	private ArrayList<JSONArray> tempPartialMedia;

	/**
	 * The number of tracks that we have recieved
	 */
	private int partialMediaSize = -1;

	/**
	 * login token
	 */
	private String loginToken;

	/**
	 * login error TODO confirm that this is a string
	 */
	private MarietjeException loginError;

	/**
	 * request errors TODO confirm that this is a String
	 */
	private String requestError;
	
	/**
	 * The currently playing track
	 */
	private JSONArray nowPlaying;

	/**
	 * Access Key TODO confirm that this is a string
	 */
	private String accessKey;

	/**
	 * Easy access to the semaphore of MarietjeClient
	 */
	private final Semaphore tracksRetrieved;
	/**
	 * Easy access to the semaphore of MarietjeClient
	 */
	private final Semaphore playingRetrieved;
	
	/**
	 * Easy access to the semaphore of MarietjeClient 
	 */
	private final Semaphore requestsRetrieved;
	
	private final Semaphore queueRetrieved;

	private JSONArray requests;

	private final Semaphore loginAttempt;
	
	
	public MarietjeClientChannel(MarietjeClient server) {
		super(new JoyceRelay(new JoyceHub()), null);

		this.server = server;
		this.loginAttempt = server.getLoginAttemptSemaphore();
		this.queueRetrieved = server.getQueueRetrievedSemaphore();
		this.tracksRetrieved = server.getTracksRetrievedSemaphore();
		this.playingRetrieved = server.getPlayingRetrievedSemaphore();
		this.requestsRetrieved = server.getRequestsRetrievedSemaphore();
		// TODO Auto-generated constructor stub
	}

	public synchronized void handleMessage(JSONObject data) throws JSONException {
		if (data.getString("type").equals("media_part")) {
			synchronized (tracksRetrieved) {
				JSONObject ding = data.getJSONObject("part");

				@SuppressWarnings("unchecked")
				Iterator<String> it  = ding.keys();
				while(it.hasNext())
					tempPartialMedia.add(ding.getJSONArray((it.next().toString())));
				if(this.partialMediaSize == tempPartialMedia.size()){
					this.partialMedia = tempPartialMedia.toArray(new JSONArray[0]);
					this.tempPartialMedia.clear();
					this.partialMediaSize = -1;
					tracksRetrieved.release();
				}
				
			}
		} else if (data.getString("type").equals("media")) {
			synchronized(tracksRetrieved) {
				this.partialMediaSize = data.getInt("count");
				if(this.partialMediaSize == tempPartialMedia.size()) {
					this.partialMedia = tempPartialMedia.toArray(new JSONArray[0]);
					this.tempPartialMedia.clear();
					this.partialMediaSize = 0;
					tracksRetrieved.release();	
				}
			}
		} else if (data.getString("type").equals("welcome"))
			return;
		else if (data.getString("type").equals("playing")) {
			synchronized (playingRetrieved) {
				this.nowPlaying = data.getJSONArray("playing");
				playingRetrieved.release();
			}
		} else if (data.getString("type").equals("requests")){
			synchronized(queueRetrieved) {
					this.requests = data.getJSONArray("requests");
					this.requestsRetrieved.release();
				}
			
		} else if (data.getString("type").equals("error_login")) {
			synchronized(loginAttempt) {
				this.loginError = new MarietjeException (data.getString("message"));
				this.loginAttempt.release();
			}
		} else if (data.getString("type").equals("logged_in")) {
			synchronized(loginAttempt) {
				this.loginToken = data.getString("login_token");
				loginAttempt.release();
			}
		} else if (data.getString("type").equals("error_request")) {
			this.requestError = data.getString("message");
		}
	}
	
	
	JSONArray getRequests () {
		return this.requests;
	}
}
