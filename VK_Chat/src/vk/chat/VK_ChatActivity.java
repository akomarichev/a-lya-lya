package vk.chat;

import vk.api.API;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class VK_ChatActivity extends TabActivity{	
	
	//SharedPreferences mPrefs;
	//final String showLoginActivity = "showLoginActivity";
	Button loginButton;	
	API api;
	TabHost tabHost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);   
        
        Resources res = getResources(); // Resource object to get Drawables
        tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        //tabHost.setOnTabChangedListener(this);

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ContactsActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("con").setIndicator("",
                          res.getDrawable(R.drawable.tab_bottom_con_s))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, ConversationsActivity.class);
        spec = tabHost.newTabSpec("msg").setIndicator("",
                          res.getDrawable(R.drawable.tab_bottom_msg_s))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SearchActivity.class);
        spec = tabHost.newTabSpec("search").setIndicator("",
                          res.getDrawable(R.drawable.tab_bottom_search_s))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = tabHost.newTabSpec("stg").setIndicator("",
                          res.getDrawable(R.drawable.tab_bottom_stg_s))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_black_bottom);
        }
        
        //if (!startLoginActivity) {        
        	//SharedPreferences.Editor editor = mPrefs.edit();
            //editor.putBoolean(showLoginActivity, true);
            //editor.commit();
	        //setupUI();
	        //Authorization();
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
    
    /*@Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#C35817"));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#C35817"));
    }*/
    
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