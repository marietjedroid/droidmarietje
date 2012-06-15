package org.marietjedroid.connect;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	private final Semaphore searchResults = new Semaphore(0);
	
	private final MarietjeClientChannel channel;
	private String accessKey = null;
	int queryToken = 0;
	
	public MarietjeClient(String host, int port, String path) {
		this.channel = new MarietjeClientChannel(this, host, port, path);
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
		ArrayList<MarietjeRequest> queue = new ArrayList<MarietjeRequest>();

		try {
			this.queueRetrieved.acquire();
			this.channel.sendMessage(new JSONObject("{'type':'follow','which':['requests']"));
			
			JSONArray requests = this.channel.getRequests();
			
			for (int i = 0; requests.optJSONObject(i) != null; i++ ){
				JSONObject req = requests.getJSONObject(i);
				JSONObject media = req.getJSONObject("media");
				String requester = req.optString("byKey", DEFAULT_REQUESTER);
				queue.add(new MarietjeRequest(requester, req.getInt("key"), media));
			}
			this.channel.sendMessage(new JSONObject("{'type':'unfollow','which':'[requests']}"));
			this.queueRetrieved.release();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queue.toArray(new MarietjeTrack[0]);		
	}
	
	public MarietjePlaying getPlaying() {
		MarietjePlaying nowPlaying = null;
		try {
			
			this.channel.sendMessage("{'type':'follow','which':['playing']}");
			this.playingRetrieved.acquire();
			JSONObject np = this.channel.getNowPlaying();
			JSONObject media = np.getJSONObject("media");
			double servertime = np.getDouble("servertime");
			double endtime = np.getDouble("endtime");
			String byKey = np.getString("byKey");
			nowPlaying = new MarietjePlaying(byKey,servertime, endtime, media);
			this.channel.sendMessage("{'type':'unfollow','which':'['playing']}");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nowPlaying;	
	}
	
	/**
	 * Logs in, or throws an exception.
	 * 
	 * @param username
	 * @param password
	 * @throws MarietjeException
	 */
	public synchronized void login (String username, String password) throws MarietjeException {
		try {
			this.channel.sendMessage("{'type':'request_login_token'}");
			try {
				this.loginAttempt.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String token = this.channel.getLoginToken();
		String hash = token.concat(password);
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hash = md5.digest(hash.getBytes()).toString();
		try {
			this.channel.sendMessage("{'type':'login', 'username':'"+username+"'," 
					+ "'hash':'"+hash+"'}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.loginAttempt.acquire();
			if (this.channel.getLoginError() != null) {
				throw this.channel.getLoginError();
			}
			this.accessKey = this.channel.getAccessKey();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Requests a track
	 * 
	 * @param trackid
	 * @throws MarietjeException
	 */
	public void requestTrack (String trackid) throws MarietjeException {
		if(this.accessKey == null)
			throw new MarietjeException("You must log in");
		try {
			this.channel.sendMessage("{'type':'request','mediaKey':'"+trackid+"'}");
			this.getQueue();
			if(this.channel.getRequestError() != null) {
				throw new MarietjeException(this.channel.getRequestError());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *  TODO
	 */
	public void uploadTrack(String artist, String title, FileInputStream f) {
		throw new NotImplementedException();
	}
	
	/**
	 * Zoeken
	 * 
	 * @param query
	 * @return
	 */
	public MarietjeTrack[] search(String query) {
		return this.search(query, 0, 10);
	}
	
	/**
	 * Zoeken
	 * 
	 * @param query
	 * @param skip
	 * @param count
	 * @return the list of tracks found, or null.
	 */
	public MarietjeTrack[] search(String query, int skip, int count) {
		this.queryToken++;
		try {
			this.channel.sendMessage("{'type':'query_media', 'token':"+queryToken+
					"'skip':"+skip+",'count':"+count+"'query':'"+query+"'}");
			this.searchResults.acquire();
			return this.channel.getQueryResults();
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
