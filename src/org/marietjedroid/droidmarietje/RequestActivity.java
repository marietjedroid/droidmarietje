package org.marietjedroid.droidmarietje;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class RequestActivity extends Activity {
	
    private static final String[] SONGS= new String[] {
        "Nothing Else Matters", "One", "Enter Sandman", "Fade to Black", "Fuel"
    };
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SONGS);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.songautocomplete);
        textView.setAdapter(adapter);
        
    }
}
