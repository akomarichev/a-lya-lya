package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import vk.adapters.ChatAdapter;
import vk.api.API;
import vk.api.User;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class ChatActivity extends Activity {
	private long chat_id;
	private String type;
	private ArrayList<User> chat_users = new ArrayList();
	private static User [] u;
	static ListView listView;
	ChatAdapter adapter;
	private Context context = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.chat_users);
	    
	    API api = new API();
	    type = getIntent().getExtras().getString("type");
	    chat_id = getIntent().getExtras().getLong("chat_id");
	    
	    try {
			chat_users = api.getChatUsers(chat_id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	    
	    u = new User[chat_users.size()];
		for(int i = 0; i<chat_users.size(); i++ ){
			u[i]=chat_users.get(i);	
		}		    
		
		listView = (ListView)findViewById(R.id.chat_listView);
		adapter = new ChatAdapter(getApplicationContext(), u);
		
		
		View footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chat_footer, null, false);
		listView.addFooterView(footerView);
		
		listView.setAdapter(adapter);
	}

}
