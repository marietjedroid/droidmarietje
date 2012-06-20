package org.marietjedroid.droidmarietje;

import org.marietjedroid.connect.MarietjeClient;
import org.marietjedroid.connect.MarietjeException;
import org.marietjedroid.connect.MarietjeTrack;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class RequestListener implements TextWatcher {

	private AutoCompleteTextView actv = null;
	private MarietjeClient mc;
	private Context c;
	private ArrayAdapter<String> aa;

	private String prevText = "";

	public RequestListener(MarietjeClient mc, AutoCompleteTextView actv,
			Context c) {
		this.mc = mc;
		this.actv = actv;
		this.c = c;

		int length = 10;
		aa = new ArrayAdapter<String>(c, length);
		actv.setAdapter(aa);
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String text = s.toString();
		Log.i("ACTV Text", text);

		if (text.equals(prevText)) {
			// niet zoeken, is al gezocht
			Log.i("Text", "Al gezocht naar " + text);
			return;
		} else {
			prevText = text;
			new RequestResult(mc, c, actv, aa).execute(text);
		}

	}

}

class RequestResult extends AsyncTask<String, Integer, String> {

	private MarietjeClient mc;
	private Context c;
	private AutoCompleteTextView actv;

	private ArrayAdapter<String> aa = null;

	public RequestResult(MarietjeClient mc, Context c, AutoCompleteTextView actv, ArrayAdapter<String> aa) {
		this.mc = mc;
		this.c = c;
		this.actv = actv;
		this.aa = aa;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			Log.i("Zoek", "naar: '" + params[0] + "'");
			MarietjeTrack[] mtArr = mc.search(params[0]);

			int length = mtArr.length;
			Log.i("Length", "" + length);

			aa.add("Kalin");
			aa.add("Kaas");
			
			for (MarietjeTrack mt : mtArr) {
				// aa.add(mt.getTitle());
				Log.i("Track", mt.getTitle());
			}

			publishProgress(100);
		} catch (MarietjeException e) {
			Log.e("onTextChanged", e.getMessage());
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
		Log.i("Progress", "Klaar met zoeken mafaka");

		
		aa.notifyDataSetChanged();

	}
}