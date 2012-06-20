package org.marietjedroid.droidmarietje;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.marietjedroid.connect.MarietjeClient;
import org.marietjedroid.connect.MarietjeException;

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
	
	private String filePath;
	
	private MarietjeClient mc = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);

		mc = MarietjeDroidActivity.getConnection();
		
		txtFileName = (TextView) findViewById(R.id.txtfilename);

		((Button) findViewById(R.id.btnpickfile)).setOnClickListener(this);

		btnUpload = (Button) findViewById(R.id.btnupload);
		btnUpload.setOnClickListener(this);
	}

	public void onClick(View v) {
		int clicked = v.getId();

		Log.i("Click", ((Button)v).getText().toString());
		
		if (clicked == R.id.btnpickfile) {
			Intent intent = new Intent();
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("text/csv");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select media file"), 1);
		} else if (clicked == R.id.btnupload) {	
			
			try {
				FileInputStream fis = new FileInputStream(filePath);
				mc.uploadTrack("Skrillex", "Sc", fis);
			} catch (FileNotFoundException e) {
				Log.e("File not found", e.getMessage());
			} catch (MarietjeException e) {
				Log.e("Upload", e.getMessage());
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}

		Uri selectedFile = data.getData();

		filePath = selectedFile.getLastPathSegment();
		txtFileName.setText("Are you sure you want to upload " + filePath + "?");

		btnUpload.setVisibility(View.VISIBLE);
		((CheckBox) findViewById(R.id.chkrequestonupload)).setVisibility(View.VISIBLE);

		Log.d("File", filePath);
	}
}
