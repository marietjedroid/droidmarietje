/**
 * @licence GNU General Public licence http://www.gnu.org/copyleft/gpl.html
 * @Copyright (C) 2012 Thom Wiggers
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.marietjedroid.connect;

import org.json.JSONException;
import org.json.JSONObject;

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
	
	public String getTrackStringLength(){
		return timeToString(this.length);
	}
	

	private static String timeToString(double time){
		double seconds = time / 1000;
    	int minutes = (int)(seconds / 60);
    	int seconds_i = (int)(seconds % 60);
    	
    	String sec_s;
    	if(seconds_i < 10){
    		sec_s = "0" + seconds_i;
    	}
    	else{
    		sec_s = "" + seconds_i;
    	}
    	
    	return minutes + ":" + sec_s;
		
	}
	
	/**
	 * @param key
	 * @param title
	 * @param artiest
	 * @param length
	 */
	public void setInfo(String key, String title, String artiest, double length){
		this.trackKey = key;
		this.title = title;
		this.artist = artiest;
		this.length = length;
	}
	
	
	/**
	 * @param artist
	 * @param title
	 * @param length
	 * @param uploader
	 * @param trackKey
	 */
	public MarietjeTrack(String artist, String title, double length, String uploader, String trackKey) {
		this.artist = artist;
		this.title = title;
		this.length = length;
		this.uploadedBy =  uploader;
	}

	/**
	 * @param media
	 */
	public MarietjeTrack(JSONObject media) {
		try {
			this.artist = media.getString("artist");
			this.title = media.getString("title");
			this.trackKey = media.getString("key");
			this.uploadedBy = media.getString("uploadedByKey");
			this.length = media.getDouble("length");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
