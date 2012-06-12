package vk.chat;

import java.util.ArrayList;

import vk.api.API;
import vk.api.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
public class FriendsActivity extends Activity {
	private ArrayList<User> friends;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.friends);
        
        API api = new API();
        
        try{
			friends = api.getFriends();
		} catch (Exception e){
			e.printStackTrace();
		} 
        
        ListView listView = (ListView)findViewById(R.id.list);
        
        User [] u = new User[friends.size()];
		for(int i = 0; i<friends.size(); i++ )
			u[i]=friends.get(i);	

		
		MySimpleArrayAdapterFast adapter = new MySimpleArrayAdapterFast(this, u);
		listView.setAdapter(adapter);
	}
}
