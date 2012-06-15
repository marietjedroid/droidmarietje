package org.marietjedroid.connect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thomwiggers.Jjoyce.base.JoyceChannel;
import org.thomwiggers.Jjoyce.base.JoyceClient;
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
	 * login error
	 */
	private MarietjeException loginError;

	/**
	 * request errors 
	 */
	private String requestError = null;
	
	/**
	 * The currently playing track
	 */
	private JSONObject nowPlaying;

	/**
	 * Access Key 
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

	private ArrayList<MarietjeTrack> queryResults = new ArrayList<MarietjeTrack>();

	private Semaphore queryResultsRetrieved;

	public MarietjeClientChannel(MarietjeClient server, String host, int port, String path) {
		super(new JoyceRelay(new JoyceClient(host, port, path)), null);
		
		this.server = server;
		this.loginAttempt = server.getLoginAttemptSemaphore();
		this.queueRetrieved = server.getQueueRetrievedSemaphore();
		this.tracksRetrieved = server.getTracksRetrievedSemaphore();
		this.playingRetrieved = server.getPlayingRetrievedSemaphore();
		this.requestsRetrieved = server.getRequestsRetrievedSemaphore();
		this.queryResultsRetrieved = server.getQueryResultsRetrievedSemaphore();
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
				this.nowPlaying = data.getJSONObject("playing");
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
				this.accessKey = data.getString("accessKey");
				loginAttempt.release();
			}
		} else if (data.getString("type").equals("error_request")) {
			this.requestError = data.getString("message");
			this.requestsRetrieved.release();
		} else if (data.getString("type").equals("query_media_results")) {
			if(data.getInt("token") != server.queryToken) {
				return; // wrong result set
			} 
			synchronized (this.queryResults) {
				this.queryResults.clear();
				JSONArray results = data.getJSONArray("results");
				for(int i = 0; results.opt(i) != null; i++) {
					JSONObject m = results.getJSONObject(i);
					this.queryResults.add(new MarietjeTrack(m));
				}
				this.queryResultsRetrieved.release();
			}
			
			
		}
	}
	
	
	JSONArray getRequests () {
		return this.requests;
	}

	/**
	 * @return the nowPlaying
	 */
	public JSONObject getNowPlaying() {
		return nowPlaying;
	}

	/**
	 * @return the loginToken
	 */
	public String getLoginToken() {
		return loginToken;
	}

	/**
	 * @return the loginError
	 */
	public MarietjeException getLoginError() {
		return loginError;
	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @return the requestError
	 */
	public String getRequestError() {
		return requestError;
	}

	public MarietjeTrack[] getQueryResults() {
		return this.queryResults.toArray(new MarietjeTrack[0]);
		
	}




}
