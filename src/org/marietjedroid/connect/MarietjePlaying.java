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

public class MarietjePlaying extends MarietjeTrack {
	/**
	 * @param byKey
	 *            The one who requested it
	 * @param servertime
	 * @param endtime
	 * @param media
	 *            media dict
	 */
	public MarietjePlaying(String byKey, double servertime, double endtime, JSONObject media) {
		super(media);
		this.serverTime = servertime;
		this.endTime = endtime;
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
