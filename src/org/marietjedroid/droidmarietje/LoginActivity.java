package org.marietjedroid.droidmarietje;

import org.marietjedroid.connect.MarietjeConnection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        Button btnlogin = (Button)findViewById(R.id.loginbutton);
        btnlogin.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		MarietjeConnection mc = MarietjeDroidActivity.getConnection();
		
		if (mc.login("username", "password")){
			Log.d("Login", "Success");
		} else {
			Log.d("Login", "Success");			
		}
	}
}
