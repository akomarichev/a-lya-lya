package vk.chat;

import java.util.ArrayList;

import vk.adapters.ConversationsAdapter;
import vk.adapters.MySimpleArrayAdapterFast;
import vk.api.API;
import vk.api.Message;
import vk.api.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationsActivity extends Activity {
	
	private ArrayList<Message> dialogs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.conversations);
        
        API api = new API();
        
        try{
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(ConversationsActivity.this, DialogActivity.class));
			}
			
		});
	}

}
