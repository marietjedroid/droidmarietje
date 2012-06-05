package org.marietjedroid.droidmarietje;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.marietjedroid.connect.MarietjeConnection;
import org.marietjedroid.connect.MarietjeTrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MarietjeDroidActivity extends Activity implements OnClickListener {
	
	private static MarietjeConnection mc = null;
	private static MarietjeTrack[] playlist = null;
	
    private static final String[] SONGS = new String[] {
        "Nothing Else Matters", "One", "Enter Sandman", "Fade to Black", "Fuel"
    };
    
    private AutoCompleteTextView requesttxt = null;
	
	private Handler mHandler = new Handler();
	private ArrayList<TextView> durationlist;
	
	public static MarietjeConnection getConnection(){
		return mc;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        durationlist = new ArrayList<TextView>();
        
        mc = new MarietjeConnection("localhost", 1234);
        if (!mc.connect()){
        	System.err.println("Cannot connect to marietje");
        	System.exit(1);
        }
        
        updateCurrentlyPlaying();
        playlist = mc.getPlaylist();

        ViewGroup muzieklijst = (ViewGroup)findViewById(R.id.muzieklijstview);
        SongListener sl = new SongListener();
        
        for (MarietjeTrack mt : playlist){
	        View v = LayoutInflater.from(this).inflate(R.layout.muziekitem, muzieklijst, false);
	        
	        RelativeLayout muzieklistrow = (RelativeLayout) v.findViewById(R.id.muziekitem);
	        
	        muzieklistrow.setTag(mt.getTrackKey());
	        
	        muzieklistrow.setOnClickListener(sl);
	        
	        ((TextView) v.findViewById(R.id.title)).setText(mt.getTitle());
	        
	        ((TextView) v.findViewById(R.id.artist)).setText(mt.getArtist());
	        
	        TextView duration = (TextView) v.findViewById(R.id.tracklength);
	        duration.setText(mt.getTrackStringTimeLeft());
	        
	        durationlist.add(duration);
	        
	        muzieklijst.addView(v);
        }
        
        

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SONGS);
        requesttxt = (AutoCompleteTextView) findViewById(R.id.requesttext);
        requesttxt.setAdapter(adapter);
        
        ((Button) findViewById(R.id.requestbutton)).setOnClickListener(this);        
        findViewById(R.id.currentlyplayingwrap).requestFocus();
        
        
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }
    
    private Runnable mUpdateTimeTask = new Runnable() {
		
		@Override
		public void run() {
			for(int i = 0; i < durationlist.size(); i++){
				TextView tv = durationlist.get(i);
				MarietjeTrack track = playlist[i];
				track.decreaseTime();
				tv.setText(track.getTrackStringTimeLeft());
			}
			
			mHandler.postDelayed(this, 1000);
			
		}
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnurequest:
            	startActivity(new Intent(getApplicationContext(), RequestActivity.class));
            	
                return true;
            case R.id.mnulogin:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            	
                return true;
                
            case R.id.mnuupload:
            	startActivity(new Intent(getApplicationContext(), UploadActivity.class));
            	
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	public static void setTimeLeft(TextView duration, MarietjeTrack mt) {
		duration.setText(mt.getTrackStringTimeLeft());
	}
    
    private void updateCurrentlyPlaying(){
    	MarietjeTrack currentlyPlaying = mc.getCurrentlyPlaying();
    	
    	((TextView) findViewById(R.id.currentlyplaying)).setText(currentlyPlaying.getArtist() + " - " + currentlyPlaying.getTitle());
    }

	@Override
	public void onClick(View v) {
		Log.d("Request", requesttxt.getText().toString());
	}
    
}