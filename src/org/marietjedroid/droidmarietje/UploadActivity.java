package org.marietjedroid.droidmarietje;

import java.net.URI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class UploadActivity extends Activity implements OnClickListener {
	private TextView txtFileName = null;
	private Button btnUpload = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        
        txtFileName = (TextView) findViewById(R.id.txtfilename);

         ((Button) findViewById(R.id.btnpickfile)).setOnClickListener(this);
        
        btnUpload = (Button) findViewById(R.id.btnupload);
        btnUpload.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		
		int clicked = v.getId();
		
		if (clicked == R.id.btnpickfile) {
			Log.d("Click", "Pick File");
			Intent intent = new Intent();
	        intent.addCategory(Intent.CATEGORY_OPENABLE);
	        intent.setType("text/csv");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        startActivityForResult(Intent.createChooser(intent, "Select media file"), 1);
		} else if (clicked == R.id.btnupload) {
			Log.d("Click", "Upload");
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Uri selectedFile = data.getData();
    	
    	String filePath = selectedFile.getLastPathSegment();
    	txtFileName.setText("Are you sure you want to upload " + filePath + "?");
    	
    	btnUpload.setVisibility(View.VISIBLE);
    	((CheckBox) findViewById(R.id.chkrequestonupload)).setVisibility(View.VISIBLE);
    	
    	Log.d("File", filePath);
    }
}
