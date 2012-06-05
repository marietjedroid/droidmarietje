package org.marietjedroid.droidmarietje;

import java.util.TimerTask;

import org.marietjedroid.connect.MarietjeTrack;

import android.widget.TextView;

public class LengthCountdown extends TimerTask{

	private TextView duration;
	private MarietjeTrack mt;
	
	public LengthCountdown(MarietjeTrack mt, TextView duration){
		this.mt = mt;
		
		this.duration = duration;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		mt.decreaseTime();
		
		//MarietjeDroidActivity.setTimeLeft(duration, mt);
		
		//duration.setText(this.mt.getTrackStringTimeLeft());
	}

}
