package vk.chat;

import java.util.ArrayList;

import vk.api.API;
import vk.api.User;
import vk.constants.Constants;
import vk.db.datasource.ChatDataSource;
import vk.db.datasource.DialogDataSource;
import vk.pref.Pref;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
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
	public static int status_mess = 0;
	public static int status_search = 0;
	
	public static View mess;
	public static View search;
	private static Resources res;
	public static DialogDataSource db_dialog;
	
	public static boolean needUpdateDialogs = false;
	public static boolean needUpdateOnlineFriends = false;
	public static boolean needUpdateTypingUser = false;
	public static boolean needUpdateTypingChat = false;
	public static boolean needUpdateConv = false;
	public static ArrayList<Integer> typing_chat = new ArrayList<Integer>();
	public static ArrayList<Integer> from_id = new ArrayList<Integer>();
	public static ArrayList<Integer> typing_user = new ArrayList<Integer>();
	
	boolean  running = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        api = new API(Pref.getAccessTokenHTTPS(VK_ChatActivity.this));
        db_dialog = new DialogDataSource(this);
        db_dialog.open();
        
        if(!Pref.loggedIn(VK_ChatActivity.this)){
	        Intent intent = new Intent();
	        intent.setClass(VK_ChatActivity.this, LoginActivity.class);
	        startActivity(intent);
	        finish();
        } else{
        	
        	res = getResources();
        	
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
	        
	        connectToTheLonPollServer();
        }
    }
    
    
    
    private void setupTabMessages(final View view, final String tag, final Intent intent) {
		View tabview = createTabViewMess(tabHost.getContext());
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabViewMess(final Context context) {
		mess = LayoutInflater.from(context).inflate(R.layout.tab_mess, null);
		iv_notify_mess = (ImageView) mess.findViewById(R.id.iv_notify);
		tv_notify_mess = (TextView) mess.findViewById(R.id.tv_notify);
		return mess;
	}
	
	private void setupTabSearch(final View view, final String tag, final Intent intent) {
		View tabview = createTabViewSearch(tabHost.getContext());
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabViewSearch(final Context context) {
		search = LayoutInflater.from(context).inflate(R.layout.tab_search, null);
		iv_notify_search = (ImageView) search.findViewById(R.id.iv_notify);
		tv_notify_search = (TextView) search.findViewById(R.id.tv_notify);
		return search;
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
    
    private void connectToTheLonPollServer(){
        new Thread(){
            @Override
            public void run(){
            	try{
            		status_mess = api.getCountUnreadMessages();
            		status_search = api.getRequestsFriends().length;
            		runOnUiThread(updateMessageStatus);
            		runOnUiThread(updateRequestsStatus);
	            	Object[] parametrs;
	        		parametrs = api.getLongPollServer();
	        		Long ts = (Long) parametrs[2];
	            	while(running){
	
		                	//String userID = Pref.getUserID(VK_ChatActivity.this);
		                	//ArrayList<User> user = api.getUsers(userID);
		                	//if(api.registerDevice().equals("1")){
	            			//parametrs = api.getLongPollServer();
		        			//Long ts = (Long) parametrs[2];
		                	//Log.d("longpoll", "longpoll");
		                	//Long ts2 = ts;
		                	ts = api.getPoll((String) parametrs[0], (String) parametrs[1], ts);
		                	///runOnUiThread(registered);       
		                	//}
		                	//else
		                		//runOnUiThread(notRegistered);
	            	}
            	}
	            catch(Exception e){
	            	
	            }
            }
        }.start();
    }
    
    
    
    
    public boolean onKeyDown(int keycode, KeyEvent event) {
	    if (keycode == KeyEvent.KEYCODE_BACK || keycode == KeyEvent.KEYCODE_HOME) {
	    	//db_dialog.close();
	    	running = false;
	    }
	    return super.onKeyDown(keycode, event);
	}
    
    @Override
    protected void onStop() {
    	//db_dialog.close();
    	//running = false;
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	//db_dialog.close();
    	//db_dialog.
    	running = false;
    	super.onDestroy();
    }
    
    @Override
    protected void onRestart() {
    	//db_dialog = new DialogDataSource(this);
        db_dialog.open();
    	running = true;
    	super.onRestart();
    }
    
    public static void setStatusMessages(int k){
    	if(k==0){
    		iv_notify_mess.setVisibility(View.INVISIBLE);
    		tv_notify_mess.setVisibility(View.INVISIBLE);
    	}
    	else{
    		tv_notify_mess.setText(k+"");
    		iv_notify_mess.setVisibility(View.VISIBLE);
    		tv_notify_mess.setVisibility(View.VISIBLE);
    	}
    	//tabHost.updateViewLayout(mess, null);
    	//tabHost.updateViewLayout(search, null);
    }
    
    public static void setStatusRequests(int k){
    	if(k==0){
    		iv_notify_search.setVisibility(View.INVISIBLE);
    		tv_notify_search.setVisibility(View.INVISIBLE);
    	}
    	else{
    		tv_notify_search.setText(k+"");
    		iv_notify_search.setVisibility(View.VISIBLE);
    		tv_notify_search.setVisibility(View.VISIBLE);
    	}
    	//tabHost.updateViewLayout(mess, null);
    	//tabHost.updateViewLayout(search, null);
    }
    
    Runnable updateMessageStatus=new Runnable(){
        @Override
        public void run() {
        	setStatusMessages(status_mess);
        }
    };
    
    Runnable updateRequestsStatus=new Runnable(){
        @Override
        public void run() {
        	setStatusRequests(status_search);
        }
    };
    
    
    
    

}