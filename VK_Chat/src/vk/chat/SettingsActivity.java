package vk.chat;

import vk.api.API;
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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	private Button logout;
	private Button changePhoto;
	private CheckBox push;
	private API api;
	private Object[] parametrs;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		
		api = new API(Pref.getAccessTokenHTTPS(SettingsActivity.this));
		
		setupUI();
	}

	private void setupUI(){
		logout = (Button) findViewById(R.id.b_logout);
		logout.setOnClickListener(logout_listener);
		changePhoto = (Button) findViewById(R.id.b_chage_photo);
		changePhoto.setOnClickListener(changePhoto_listener);
		push = (CheckBox) findViewById(R.id.checkBox_push);
		push.setOnClickListener(pushNotifications);
	}
	
	private OnClickListener logout_listener=new OnClickListener(){
        @Override
        public void onClick(View v) {
        	// remove all shared prefs;
        	Pref.logOut(SettingsActivity.this);
        	Pref.deleteHTTPSAuth(SettingsActivity.this);
        	Pref.deleteNeedUpdateChatListActivity(SettingsActivity.this);
        	
        	startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        	finish();
        }
    };
    
    private OnClickListener changePhoto_listener=new OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };
    
    
    private OnClickListener pushNotifications=new OnClickListener(){
        @Override
        public void onClick(View v) {
        	if (((CheckBox) v).isChecked())
        		pushNotificationRegistration();
        	else
        		pushNotificationUnRegistration();
        }
    };
    
    private void pushNotificationRegistration() {
        new Thread(){
            @Override
            public void run(){
                try {
                	if(api.registerDevice().equals("1")){
                		parametrs = api.getLongPollServer();
                		api.getPoll((String) parametrs[0], (String) parametrs[1], (Long) parametrs[2]);
                		runOnUiThread(registered);       
                	}
                	else
                		runOnUiThread(notRegistered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private void pushNotificationUnRegistration() {
        new Thread(){
            @Override
            public void run(){
                try {
                	if(api.unregisterDevice().equals("1")){
                		api.getLongPollServer();
                		runOnUiThread(notRegistered);
                	}               		            	       
                	else
                		runOnUiThread(registered);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    
    Runnable registered=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(SettingsActivity.this,
     		 	   "Registered", Toast.LENGTH_LONG).show();
        }
    };
    
    
    Runnable notRegistered=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(SettingsActivity.this,
     		 	   "Not registered!", Toast.LENGTH_LONG).show();
        }
    };

}
