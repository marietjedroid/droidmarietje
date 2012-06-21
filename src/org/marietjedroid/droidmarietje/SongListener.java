package org.marietjedroid.droidmarietje;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SongListener implements OnClickListener {
	private Context c;

	public SongListener(Context c) {
		this.c = c;
	}

	public void onClick(View v) {

		String title = ((TextView) v.findViewById(R.id.title)).getText()
				.toString();
		String artist = ((TextView) v.findViewById(R.id.artist)).getText()
				.toString();

		Log.d("Onclick", "with id: " + title);

		Bundle b = new Bundle();
		b.putString("artist", artist);
		b.putString("title", title);

		Intent i = new Intent(c, LastFmActivity.class);
		i.putExtras(b);

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		c.startActivity(i);
	}
}
