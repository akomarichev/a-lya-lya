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
import android.os.Bundle;
import android.util.Log;
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
	private long chat_id;
	private String title;
	private String type;
	private ArrayList<User> chat_users = new ArrayList();
	private ArrayList<User> buf = new ArrayList();
	private static User [] u;
	static ListView listView;
	ChatListAdapter adapter;
	private Context context = null;
	
	private ImageView back;
	private TextView members;
	private EditText et_title;
	private Button change_title;
	private Button add_member;
	private String myID;
	private View footerView;
	private API api;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.chat_users);
	    
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
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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
		
		
		
		listView.addFooterView(footerView);
		
		listView.setAdapter(adapter);
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
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_success_change_title), Toast.LENGTH_LONG).show();
        }
    };
    
    Runnable notSuccessEditTitle=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_fail_change_title), Toast.LENGTH_LONG).show();
        }
    };
	
	private OnClickListener addClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
		}
	};

}
