package vk.chat;

import vk.api.API;
import vk.constants.Constants;
import vk.pref.Pref;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class VK_ChatActivity extends TabActivity{	
	
	//SharedPreferences mPrefs;
	//final String showLoginActivity = "showLoginActivity";
	private Button loginButton;	
	private API api;
	private static TabHost tabHost;
	
	public static ImageView iv_notify_mess;
	public static ImageView iv_notify_search;
	public static TextView tv_notify_mess;
	public static TextView tv_notify_search;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        if(!Pref.loggedIn(VK_ChatActivity.this)){
	        Intent intent = new Intent();
	        intent.setClass(VK_ChatActivity.this, LoginActivity.class);
	        startActivity(intent);
	        finish();
        } else{
        	tabHost = (TabHost) findViewById(android.R.id.tabhost);
            tabHost.setup();

	        Intent intent;
	
	        intent = new Intent().setClass(this, ContactsActivity.class);
	        setupTabFr(new TextView(this), "con", intent);
	
	        intent = new Intent().setClass(this, ConversationsActivity.class);
	        setupTabMessages(new TextView(this), "msg", intent);
	
	        intent = new Intent().setClass(this, SearchActivity.class);
	        setupTabSearch(new TextView(this), "search", intent);
	        
	        intent = new Intent().setClass(this, SettingsActivity.class);
	        setupTabSett(new TextView(this), "stg", intent);
        }
    }
    
    private void setupTabMessages(final View view, final String tag, final Intent intent) {
		View tabview = createTabViewMess(tabHost.getContext());
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabViewMess(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_mess, null);
		iv_notify_mess = (ImageView) view.findViewById(R.id.iv_notify);
		tv_notify_mess = (TextView) view.findViewById(R.id.tv_notify);
		return view;
	}
	
	private void setupTabSearch(final View view, final String tag, final Intent intent) {
		View tabview = createTabViewSearch(tabHost.getContext());
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabViewSearch(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_search, null);
		iv_notify_search = (ImageView) view.findViewById(R.id.iv_notify);
		tv_notify_search = (TextView) view.findViewById(R.id.tv_notify);
		return view;
	}
	
	private void setupTabFr(final View view, final String tag, final Intent intent) {
		View tabview = createTabViewFr(tabHost.getContext());
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabViewFr(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_fr, null);
		return view;
	}
	
	private void setupTabSett(final View view, final String tag, final Intent intent) {
		View tabview = createTabViewSett(tabHost.getContext());
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabViewSett(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_sett, null);
		return view;
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
            api=new API(Constants.ACCESS_TOKEN);
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