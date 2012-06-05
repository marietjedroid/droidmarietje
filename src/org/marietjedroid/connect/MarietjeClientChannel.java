package org.marietjedroid.connect;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thomwiggers.Jjoyce.base.JoyceChannel;
import org.thomwiggers.Jjoyce.base.JoyceRelay;

class MarietjeClientChannel extends JoyceChannel{
	
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
	private int partialMediaSize;
	
	/**
	 * login token 
	 */
	private String loginToken;
	
	/**
	 * login error 
	 * TODO confirm that this is a string
	 */
	private String loginError;
	
	/**
	 * request errors
	 * TODO confirm that this is a String
	 */
	private String requestError;
	
	/**
	 * Access Key
	 * TODO confirm that this is a string
	 */
	private String accessKey;
	
	
	public MarietjeClientChannel(JoyceRelay relay, String token) {
		super(relay, token);
		// TODO Auto-generated constructor stub
	}
	
	public void handleMessage(JSONObject data) throws JSONException {
		if(data.getString("type").equals("media_part")) {
			//TODO locken en condition.await() doen.
			ArrayList<JSONArray> partialMedia= new ArrayList<JSONArray>();
			JSONArray ding = data.getJSONArray("part");
			int i = 0;
			while(ding.optJSONArray(i) != null)
				partialMedia.add(ding.optJSONArray(i++));
			//TODO
		}
	}
	
}
