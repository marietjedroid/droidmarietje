package org.marietjedroid.droidmarietje;

import java.util.HashMap;

import org.marietjedroid.connect.MarietjeClient;
import org.marietjedroid.connect.MarietjeException;
import org.marietjedroid.connect.MarietjeTrack;

import android.R.anim;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RequestListener implements TextWatcher, OnItemClickListener {

	private AutoCompleteTextView actv = null;
	private MarietjeClient mc;
	private Context c;
	private ArrayAdapter<String> aa;

	private HashMap hm = new HashMap<String, String>();
	private String reqId = null;
	
	public RequestListener(MarietjeClient mc, AutoCompleteTextView actv,
			Context c) {
		this.mc = mc;
		this.actv = actv;
		this.c = c;
		
		aa = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line);

		actv.setAdapter(aa);
		
		actv.setOnItemClickListener(this);		
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

		
		if (text.equals("")){
			Log.i("TextEmpty", "true");
			return;
		}
			 
		if(text.length() == 1){
			new RequestResult(mc, c, actv, aa, hm, reqId).execute(text.toLowerCase());
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String reqId = (String) hm.get(((TextView)arg0.getChildAt(arg2)).getText().toString());
		
		Log.i("reqId", reqId);
		
		this.reqId = reqId;
	}
	
	public String getReqId(){
		return this.reqId;
	}

}

class RequestResult extends AsyncTask<String, Integer, String> {

	private MarietjeClient mc;
	private Context c;
	private AutoCompleteTextView actv;

	private ArrayAdapter<String> aa = null;
	
	private HashMap<String, String> hm;
	
	private String[] s;
	private String reqId;

	public RequestResult(MarietjeClient mc, Context c, AutoCompleteTextView actv, ArrayAdapter<String> aa, HashMap<String, String> hm, String reqId) {
		this.mc = mc;
		this.c = c;
		this.actv = actv;
		this.hm = hm;
		this.aa = aa;
		this.reqId = reqId;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			Log.i("Zoek", "naar: '" + params[0] + "'");
			MarietjeTrack[] mtArr = mc.search(params[0]);

			int length = mtArr.length;
			Log.i("Length", "" + length);
			
			if(length == 0){
				this.reqId = null;
			}
			
			
			
			s = new String[length];
					
			int i = 0;
			for (MarietjeTrack mt : mtArr) {
				aa.add(mt.getTitle());
				hm.put(mt.getTitle(), mt.getTrackKey());
				s[i] = mt.getTitle();
				i++;
				Log.i("Track", mt.getTitle());
			}

			publishProgress(100);
		} catch (Exception e) {
			Log.e("onTextChanged", e.getMessage());
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
		Log.i("Progress", "Klaar met zoeken mafaka");
		
		ArrayAdapter<String> fiets = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, s);
		fiets.setNotifyOnChange(true);
		actv.setAdapter(fiets);
		
		actv.showDropDown();
	}
}