package vk.chat;

import vk.api.API;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class VK_ChatActivity extends Activity {	
	
	//SharedPreferences mPrefs;
	//final String showLoginActivity = "showLoginActivity";
	Button loginButton;	
	API api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);   
        
       // mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Boolean startLoginActivity = mPrefs.getBoolean(showLoginActivity, false);
        
        //if (!startLoginActivity) {        
        	//SharedPreferences.Editor editor = mPrefs.edit();
            //editor.putBoolean(showLoginActivity, true);
            //editor.commit();
	        setupUI();
	        Authorization();
        //}
    }
    
    public void setupUI(){
    	loginButton = (Button) findViewById(R.id.button1);
    	loginButton.setOnClickListener(loginClick);
    }
    
    private OnClickListener loginClick=new OnClickListener(){
        @Override
        public void onClick(View v) {
        	//startActivity(new Intent(VK_ChatActivity.this, LoginActivity.class));
        	startActivity(new Intent(VK_ChatActivity.this, SignupActivity.class));
        }
    };
    
    private void Authorization(){
    	Account.restore(this);
    	if(Account.access_token!=null){
            api=new API();
    	}
    	else
    		startLoginActivity(); 		
    }
    
    private void startLoginActivity() {
        /*Intent intent = new Intent(VK_ChatActivity.this, LoginActivity.class);
         * 
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);*/
    	//startActivity(new Intent(VK_ChatActivity.this, LoginActivity.class));
    	startActivity(new Intent(VK_ChatActivity.this, SignupActivity.class));
    }
    
    /*@Override
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                Account.access_token=data.getStringExtra("token");
                Account.user_id=data.getLongExtra("user_id", 0);              
                Account.save(VK_ChatActivity.this);
                api=new API();
            }
        }
    }*/
}