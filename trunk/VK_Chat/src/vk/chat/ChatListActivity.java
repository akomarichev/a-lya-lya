package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import vk.adapters.ChatListAdapter;
import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.constants.Constants;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatListActivity extends Activity {
	private static final int PICK_USERID = 1;
	
	private long chat_id;
	private String title;
	private String type;
	private ArrayList<User> chat_users = new ArrayList();
	private ArrayList<User> buf = new ArrayList();
	private static User [] u;
	static ListView listView;
	ChatListAdapter adapter;
	private Context context = null;
	
	private boolean isFooterSet = false;
	
	private ImageView back;
	private TextView members;
	private EditText et_title;
	private Button change_title;
	private Button add_member;
	private String myID;
	private View footerView;
	private API api;
	
	private Thread updater;
	boolean running = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.chat_users);
	    
	    updater();
	    
	    if(getIntent().hasExtra("chat_id"))
	    	chat_id = getIntent().getExtras().getLong("chat_id");
	    if(getIntent().hasExtra("title"))
	    	title = getIntent().getExtras().getString("title");	 
	    
	    setupUI();
	    myID = Pref.getUserID(this);
	    
	    api = new API(Pref.getAccessTokenHTTPS(ChatListActivity.this));
	    //type = getIntent().getExtras().getString("type");
	    
	    
	    try {
			chat_users = api.getChatUsers(chat_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    members.setText(chat_users.size()-1 + " " + getResources().getString(R.string.vd_members));
	    
	    
	    for(User user:chat_users){
	    	if(Long.toString(user.uid).equals(myID))
				continue;
	    	buf.add(user);
	    }
	    
	    u = new User[buf.size()];
	    
		for(int i = 0; i<buf.size(); i++ )			
			u[i]=buf.get(i);	
			    
		
		listView = (ListView)findViewById(R.id.chat_listView);
		adapter = new ChatListAdapter(getApplicationContext(), u, chat_id);
		
		
		if(!isFooterSet){
			listView.addFooterView(footerView);
			isFooterSet = true;
		}
		
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	    Long uid = 0L;
		if (requestCode == PICK_USERID) {
			if(data.hasExtra("uid"))
			uid = data.getLongExtra("uid", 0L);
			addMember(uid);
		}
	}
	
	void setupUI(){
		members = (TextView) findViewById(R.id.tv_members);
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(backClick);
		
		footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chat_footer, null, false);
		et_title = (EditText) footerView.findViewById(R.id.et_changeTitle);
		et_title.setText(title);
		change_title = (Button) footerView.findViewById(R.id.b_changeTitle);
		change_title.setOnClickListener(changeClick);		
		add_member = (Button) footerView.findViewById(R.id.b_addMember);
		add_member.setOnClickListener(addClick);
	}
	
	private OnClickListener backClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			running = false;
			finish();
		}
	};
	
	private OnClickListener changeClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			changeTitle();
		}
	};
	
	private void changeTitle() {
        new Thread(){
            @Override
            public void run(){
            	String response = "";
            	try {
					response = api.editChat(chat_id, et_title.getText().toString());
				} catch (Exception e) {
					e.printStackTrace();
				} 
            	if(response.equals("1"))
            		runOnUiThread(successEditTitle);
            	else
            		runOnUiThread(notSuccessEditTitle);
            }
        }.start();
    }
	
	Runnable successEditTitle=new Runnable(){
        @Override
        public void run() {
        	Pref.setNeedUpdateChatListActivity(ChatListActivity.this);
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_success_change_title), Toast.LENGTH_LONG).show();
        }
    };
    
    Runnable notSuccessEditTitle=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_fail_change_title), Toast.LENGTH_LONG).show();
        }
    };
    
    private void addMember(final Long uid) {
        new Thread(){
            @Override
            public void run(){
            	String response = "";
            	try {
					response = api.addChatUser(chat_id, uid);
				} catch (Exception e) {
					e.printStackTrace();
				} 
            	if(response.equals("1"))
            		runOnUiThread(successAdded);
            	else
            		runOnUiThread(notSuccessAdded);
            }
        }.start();
    }
	
	Runnable successAdded=new Runnable(){
        @Override
        public void run() {
        	Pref.setNeedUpdateChatListActivity(ChatListActivity.this);
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_success_add_member), Toast.LENGTH_LONG).show();
        }
    };
    
    Runnable notSuccessAdded=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_fail_add_member), Toast.LENGTH_LONG).show();
        }
    };
	
	private OnClickListener addClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			getUserID();
		}
	};
	
	private void getUserID(){
		Intent intent = new Intent(ChatListActivity.this, FriendsActivity.class);
		intent.putExtra("addUserToChat", "addUserToChat");
        startActivityForResult(intent, PICK_USERID);
	}
	
	private void updater() {
        new Thread(){
            @Override
            public void run(){
            	while(running){
	            	Log.d("updater", "updater");
	            	if(Pref.isSetNeedUpdateChatListActivity(ChatListActivity.this)){
	            		runOnUiThread(updateActivity);
	        			runOnUiThread(updateList);
	        			Pref.resetNeedUpdateChatListActivity(ChatListActivity.this);
	        		}
	    			try {
	    				Thread.sleep(100);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}      
            	}
            }
        }.start();
    }
	
	Runnable updateActivity=new Runnable(){
        @Override
        public void run() {
        	try {
    			chat_users = api.getChatUsers(chat_id);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}    	    
    	    members.setText(chat_users.size()-1 + " " + getResources().getString(R.string.vd_members));
        }
    };
    
    Runnable updateList=new Runnable(){
        @Override
        public void run() {
        	buf.clear();
        	
        	for(User user:chat_users){
    	    	if(Long.toString(user.uid).equals(myID))
    				continue;
    	    	buf.add(user);
    	    }
    	    
    	    u = new User[buf.size()];
    	    
    		for(int i = 0; i<buf.size(); i++ )			
    			u[i]=buf.get(i);	
    			    
    		
    		listView = (ListView)findViewById(R.id.chat_listView);
    		adapter = new ChatListAdapter(getApplicationContext(), u, chat_id);

    		if(!isFooterSet){
    			listView.addFooterView(footerView);
    			isFooterSet = true;
    		}
    		
    		//
    		
    		Log.d("update_list", u.toString());
    		listView.setAdapter(adapter);
    		adapter.notifyDataSetChanged();
        }
    };
    
    public boolean onKeyDown(int keycode, KeyEvent event) {
	    if (keycode == KeyEvent.KEYCODE_BACK || keycode == KeyEvent.KEYCODE_HOME) {
	    	running = false;
	    }
	    return super.onKeyDown(keycode, event);
	}
    
    @Override
    protected void onStop() {
    	running = false;
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	running = false;
    	super.onDestroy();
    }
    
    @Override
    protected void onRestart() {
    	running = true;
    	updater();
    	super.onRestart();
    }
}
