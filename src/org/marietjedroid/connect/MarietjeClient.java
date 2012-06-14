package org.marietjedroid.connect;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thomwiggers.Jjoyce.base.JoyceChannel;
import org.thomwiggers.Jjoyce.base.NotImplementedException;

public class MarietjeClient {
	private static final String DEFAULT_REQUESTER = "marietje";
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
	
	/**
	 * Blocks until the queue has been retrieved.
	 */
	private final Semaphore queueRetrieved = new Semaphore(0);
	
	/**
	 * Blocks until we get answer from a login attempt 
	 */
	private final Semaphore loginAttempt = new Semaphore(0);
	
	private final MarietjeClientChannel channel;
	
	public MarietjeClient() {
		this.channel = new MarietjeClientChannel(this);
	}
	
	JoyceChannel getChannel() {
		return channel;
	}
	
	/**
	 * 
	 * @return
	 */
	Semaphore getTracksRetrievedSemaphore() {
		return tracksRetrieved;
	}

	/**
	 * @return the playingRetrieved
	 */
	Semaphore getPlayingRetrievedSemaphore() {
		return playingRetrieved;
	}

	Semaphore getRequestsRetrievedSemaphore() {
		return requestsRetrieved;
	}

	/**
	 * @return the queueRetrieved
	 */
	Semaphore getQueueRetrievedSemaphore() {
		return queueRetrieved;
	}

	Semaphore getLoginAttemptSemaphore() {
		return loginAttempt;
	}
	
	/**
	 * Gets the queue
	 * 
	 * @return
	 */
	public MarietjeTrack[] getQueue() {
		try {
			this.channel.sendMessage(new JSONObject("{'type':'follow','which':['requests']"));
			this.queueRetrieved.acquire();
			JSONArray requests = this.channel.getRequests();
			ArrayList<MarietjeRequest> queue = new ArrayList<MarietjeRequest>();
			for (int i = 0; requests.optJSONObject(i) != null; i++ ){
				JSONObject req = requests.getJSONObject(i);
				JSONObject media = req.getJSONObject("media");
				String requester = req.optString("byKey", DEFAULT_REQUESTER);
				queue.add(new MarietjeRequest(requester, req.getInt("key"), media));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}
	
		
}
