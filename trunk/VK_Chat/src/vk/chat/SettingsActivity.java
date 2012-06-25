package vk.chat;

import vk.pref.Pref;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private Button logout;
	private Button changePhoto;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		
		setupUI();
	}

	private void setupUI(){
		logout = (Button) findViewById(R.id.b_logout);
		logout.setOnClickListener(logout_listener);
		changePhoto = (Button) findViewById(R.id.b_chage_photo);
		changePhoto.setOnClickListener(changePhoto_listener);
	}
	
	private OnClickListener logout_listener=new OnClickListener(){
        @Override
        public void onClick(View v) {
        	Pref.logOut(SettingsActivity.this);
        	startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        	finish();
        }
    };
    
    private OnClickListener changePhoto_listener=new OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };
    

}
