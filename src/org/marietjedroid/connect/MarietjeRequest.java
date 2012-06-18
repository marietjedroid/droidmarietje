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

import org.json.JSONObject;

public class MarietjeRequest extends MarietjeTrack {
	
	/**
	 * @param artist
	 * @param title
	 * @param length
	 * @param uploader
	 * @param key Request key
	 * @param requester
	 */
	public MarietjeRequest(String artist, String title, double length,
			String uploader, int requestKey, String requester) {
		super(artist, title, length, uploader, uploader);
		
		this.key = requestKey;
		this.requester = requester;
		// TODO Auto-generated constructor stub
	}
	
	public MarietjeRequest (String requester, int requestKey, JSONObject media){
		super(media);
		this.requester = requester;
		this.key = requestKey;
	}

	/**
	 * identifier in the queue 
	 */
	private final int key;
	
	/**
	 * Requester
	 */
	private final  String requester;

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @return the requester
	 */
	public String getRequester() {
		return requester;
	}
	
	
}
