package vk.chat;

import java.util.ArrayList;

import vk.adapters.ConversationsAdapter;
import vk.adapters.MySimpleArrayAdapterFast;
import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.constants.Constants;
import vk.popup.ActionItem;
import vk.popup.QuickAction;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationsActivity extends Activity {
	
	
	private ArrayList<Message> dialogs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.conversations);
        
        
		
		//Add action item
        //ActionItem addAction = new ActionItem();
        
        API api = new API(Pref.getAccessTokenHTTPS(ConversationsActivity.this));
        
        try{
        	//api.SendHttpPost(null);
			dialogs = api.getMessagesDialogs(0, 10);			
		} catch (Exception e){
			e.printStackTrace();
		} 
        
        Message m[] = new Message[dialogs.size()];
		for(int i = 0; i<dialogs.size(); i++ )
			m[i]=dialogs.get(i);	
        
        Log.d("Dialogs",dialogs.toString());
        
        ListView listView = (ListView)findViewById(R.id.list_dialogs);
        ConversationsAdapter adapter = new ConversationsAdapter(this, m);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Message msg = new Message();
				msg = dialogs.get(position);
				
				Intent intent = new Intent(ConversationsActivity.this, DialogActivity.class);
				if(msg.chat_id != null){				
					Intent intent2 = new Intent(ConversationsActivity.this, ChatActivity.class);
					intent2.putExtra("chat_id", msg.chat_id);
					intent2.putExtra("type", "chat_id");
					startActivity(intent2);
				}
				else{					
					intent.putExtra("uid", msg.uid);
					intent.putExtra("type", "uid");
					startActivity(intent);
				}				
				
				//startActivity(new Intent(ConversationsActivity.this, AudioActivity.class));
				//
				//Toast.makeText(getApplicationContext(),
				//		"Click ListItem Number " + msg.uid, Toast.LENGTH_LONG)
				//		.show();
			}			
		});
	}

}
