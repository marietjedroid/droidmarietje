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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class LastFmActivity extends Activity {
	private static String APIKEY = "f6e8b671a6ecf88a3660623406b93f16";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songinfo);

		// Bundle b = getIntent().getExtras();

		// String artist = b.getString("artist");
		// String title = b.getString("title");

		String artist = "Skrillex";
		String title = "Scary monsters and nice sprites";

		String artistUrl = URLEncoder.encode(artist);
		String songUrl = URLEncoder.encode(title);

		StringBuilder sb = new StringBuilder(
				"http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=");
		sb.append(APIKEY).append("&artist=" + artistUrl + "&track=" + songUrl);

		LastFmParser lfp = new LastFmParser(sb.toString());

		lfp.parse();

		((TextView) findViewById(R.id.title)).setText(lfp.getTitle());
		((TextView) findViewById(R.id.artist)).setText(lfp.getArtist() + " " + lfp.getLength());
		((TextView) findViewById(R.id.wiki)).setText(lfp.getWiki());

		((TextView) findViewById(R.id.album_title)).setText(lfp.getAlbum());

		ImageView i = (ImageView) findViewById(R.id.albumart);

		String albumArt = lfp.getAlbumArt();

		try {
			URL newurl = new URL(albumArt);

			Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
			i.setImageBitmap(mIcon_val);
		} catch (IOException e) {
		}
	}
}

class getInfo extends AsyncTask<String, Integer, String> {
	@Override
	protected String doInBackground(String... params) {
		try {
			URL lastfmpage = new URL(params[0]);
			BufferedReader in = new BufferedReader(new InputStreamReader(lastfmpage.openStream()));

			String inputLine;
			StringBuilder webPage = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				webPage.append(inputLine);
			}
			in.close();

			// shit doen met de xml
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
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