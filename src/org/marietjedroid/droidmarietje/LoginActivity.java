package org.marietjedroid.droidmarietje;

import org.marietjedroid.connect.MarietjeClient;
import org.marietjedroid.connect.MarietjeException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText txtUsername, txtPassword;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		txtUsername = (EditText) findViewById(R.id.txtusername);
		txtPassword = (EditText) findViewById(R.id.txtpassword);

		((Button) findViewById(R.id.loginbutton)).setOnClickListener(this);
	}

	public void onClick(View v) {
		MarietjeClient mc = MarietjeDroidActivity.getConnection();

		String username = txtUsername.getText().toString().trim();
		String password = txtPassword.getText().toString().trim();
	
		if (username.equals("") || password.equals("")) {
			// not all fields have been filled out
			return;
		}
		
		AsyncTask<String, Integer, String> asyncTask = new LoginTask(mc);
		
		asyncTask.execute(new String[] {username, password});
		
		
		
	}
}

class LoginTask extends AsyncTask<String, Integer, String>{

	private MarietjeClient mc;

	public LoginTask(MarietjeClient mc){
		this.mc = mc;
	}
	
	@Override
	protected String doInBackground(String... params) {
		Log.i("Inloggen", "Nu mee bezig... " + params[0] + ":" + params[1]);
		try {
			mc.login(params[0], params[1]); // TODO pas op, locks
			Log.i("Login", "Success");
		} catch (MarietjeException me) {
			Log.e("Login", "Failed");
		}
		return null;
	}
	
}
