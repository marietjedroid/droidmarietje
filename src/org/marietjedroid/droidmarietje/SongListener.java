package org.marietjedroid.droidmarietje;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SongListener implements OnClickListener {

	
	
	@Override
	public void onClick(View v) {
		
		TextView tv = (TextView)v.findViewById(R.id.title);
		Log.d("Onclick", "with id: "+ tv.getText().toString());
		
	}

}
