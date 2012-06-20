package org.marietjedroid.droidmarietje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class LastFmActivity extends Activity {
	private static String APIKEY = "f6e8b671a6ecf88a3660623406b93f16";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songinfo);

		Bundle b = getIntent().getExtras();

		String artist = b.getString("artist");
		String title = b.getString("title");

		Log.i("SONG", artist + " " + title);

		String artistUrl = URLEncoder.encode(artist);
		String songUrl = URLEncoder.encode(title);

		StringBuilder sb = new StringBuilder(
				"http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=");
		sb.append(APIKEY).append("&artist=" + artistUrl + "&track=" + songUrl);

		Log.i("Request url", sb.toString());

		new getInfo().execute(sb.toString());
	}
}

class getInfo extends AsyncTask<String, Integer, String> {
	@Override
	protected String doInBackground(String... params) {
		try {
			URL lastfmpage = new URL(params[0]);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					lastfmpage.openStream()));

			String inputLine;
			StringBuilder webPage = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				webPage.append(inputLine);
			}
			in.close();

			// shit doen met de xml
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = builderFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			Log.e("MALFORMED URL", "YOU SUCK");
		} catch (IOException e) {
			Log.e("Oei", "A wild error appeared!");
		}
		return null;
	}
}