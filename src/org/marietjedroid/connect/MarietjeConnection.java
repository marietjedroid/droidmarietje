package org.marietjedroid.connect;

/**
 * @author Thom
 *
 */
public class MarietjeConnection {
	
	/** 
	 * @param host
	 * @param Port
	 */
	public MarietjeConnection(String host, int Port) {
		// TODO
	}
	
	/**
	 * Gets the current playlist
	 * 
	 * @return Currently queued tracks
	 */
	public MarietjeTrack[] getPlaylist() {
		//TODO
		
		MarietjeTrack[] lijst = null;
		lijst = new MarietjeTrack[11];
		
		MarietjeTrack mt;
		for (int i = 0; i <= 10; i++){
			mt = new MarietjeTrack();
			mt.setInfo(i + "", "Lied " + i, "Artiest " + i, i * 1000);
			lijst[i] = mt;
		}
		
		return lijst;	
	}
	
	public MarietjeTrack getCurrentlyPlaying(){
		MarietjeTrack mt = new MarietjeTrack();
		mt.setInfo("gfds", "Scary Monsters And Nice Sprites", "Skrillex", 12345);
		
		return mt;
	}
	
	public MarietjeTrack[] getQueryResults(String query) {
		// TODO 
		return null;
		
	}
	
	public boolean login(String username, String password) {
		//TODO
		 return true;
	}
	
	public boolean connect() {
		return true;
		
	}
}
