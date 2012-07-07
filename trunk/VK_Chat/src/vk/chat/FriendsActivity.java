package vk.chat;

import java.util.ArrayList;
import java.util.Collections;

import vk.adapters.MySimpleArrayAdapterFast;
import vk.api.API;
import vk.api.User;
import vk.constants.Constants;
import vk.db.datasource.FriendsDataSource;
import vk.pref.Pref;
import vk.utils.UserFullNameComparator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
public class FriendsActivity extends Activity implements OnScrollListener{
	
	private static boolean FORWARD_MESSAGES = false;
	private static boolean ADD_USER_TO_CHAT = false;
	private static boolean INVITE_FRIEND = false;
	
	private String forwarded_messages = null;
	//private static final int PICK_FROM_FILE = 2;
	//private static final int PICK_LOCATION = 3;
	
	private ArrayList<User> friends;
	private ArrayList<User> friends_db;
	private static int max;
	private static final int UPDATE_LIST = 100;
	private static Integer currentPosition = 0;
	private static User [] u;
	static ListView listView;
	MySimpleArrayAdapterFast adapter;
	private FriendsDataSource db_friends;
	
	private static String lastLetter;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.friends);
   
	    final Handler handler = new Handler();
	    db_friends = new FriendsDataSource(this);
	    db_friends.open();
	    
	    
	    
	    listView = (ListView)findViewById(R.id.list);
	    listView.setOnItemClickListener(clickDilog);
	    
	    checkExtras();
	    
	    new AsyncTask<Context, Void, Void>() {

	        @Override
	        protected Void doInBackground(Context... params) {
		            API api = new API(Pref.getAccessTokenHTTPS(FriendsActivity.this)); 

		            //Pref.cancelLoadedFriendsDB(FriendsActivity.this);
		            
		            if(!Pref.loadedFriendsDB(FriendsActivity.this)){
		            	try{
			    			friends = api.getFriends();		    			
			    		} catch (Exception e){
			    			e.printStackTrace();
			    		}
		            	db_friends.removeAll();
		            	friends_db = new ArrayList<User>(friends);
		            	loadFriendDb(friends_db);
		            }
		            else{
		            	friends = db_friends.getAllFriends();
		            	db_friends.close();
		            }
		            	
		            //Log.d("FriendsActivity.this", friends.toString());
	            	            
		            
		            
		           	
		            
		            //u = new User[friends.size()];
		            /*Integer max_friends;
		            if(max > UPDATE_LIST)
		            	max_friends = UPDATE_LIST;
		            else
		            	max_friends = max;*/
		            
		            u = new User[friends.size()];
		            
		            // добавка избранных друзей 5 штук
		            for(int i = 0; i<5; i++ ){
		    			//currentPosition += 1;
		    			u[i]=friends.get(0);
		    			friends.remove(0);
		    		}		
		            
		            // сортируем оставшихся друзей
		            Collections.sort(friends, new UserFullNameComparator());
		            
		            // добавляем отсортированных друзей.
		            max = friends.size();
		            
		    		for(int i = 0; i<max; i++ ){
		    			//currentPosition += 1;
		    			u[i+5]=friends.get(i);	
		    		}		    	
		    		
		    		// добавляем разделители
		    		max = friends.size()+5;
		    		for(int i = 0; i<max; i++){
			    		if(i >= 5){
			    	        String letter = Character.toString(u[i].first_name.charAt(0)).toUpperCase();
			    	        if(!letter.equals(lastLetter)){
			    	        	MySimpleArrayAdapterFast.letter_buffer.put(i, letter);
			    			    lastLetter = letter;
			    	        }
			    		}
		    		}
		    		
		    		
	                return null;
	            } 
	        
	            @Override
	            public void onPostExecute(Void result){
	                handler.post(new Runnable(){
	                     @Override
	                     public void run(){
	     		            adapter = new MySimpleArrayAdapterFast(getApplicationContext(), u);
	     		            listView.setAdapter(adapter);
	                     }
	                });
	            }
	       }.execute();
        
	       
	       
	       

		
		
		// -------------------------------
		
		//listView.setTextFilterEnabled(true);
		//listView.setOnScrollListener(this);
		/*listView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
                Message msg = new Message();
                msg.what = UPDATE_LIST;
                updateListHandler.sendMessage(msg);				
			}
		});*/
	}
	
	private OnItemClickListener clickDilog=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			User friend = new User();
			friend = u[position];
			FORWARD_MESSAGES = false;
			if(ADD_USER_TO_CHAT){
				Intent intent = new Intent();
				intent.putExtra("uid", friend.uid);
			    setResult(RESULT_OK, intent);
			    ADD_USER_TO_CHAT = false;
			    finish();
			}
			else if(INVITE_FRIEND){
				Intent intent = new Intent(FriendsActivity.this, UserRegisteredActivity.class);
				intent.putExtra("name", u[position].first_name + " " + u[position].last_name);
				intent.putExtra("photo", u[position].photo_medium);
				intent.putExtra("uid", u[position].uid);
				intent.putExtra("phone", u[position].mobile_phone);
				startActivity(intent);
			}
			else{
				Intent intent = new Intent(FriendsActivity.this, DialogActivity.class);
				intent.putExtra("uid", friend.uid);
				intent.putExtra("type", "uid");
				if(forwarded_messages!=null){
					intent.putExtra("f_msgs", forwarded_messages);
					startActivity(intent);
					finish();
				}
				else
					startActivity(intent);
			}
		}		
	};
	
	
	void addMoreDataToList() {
		int max_friends;
        if(max > UPDATE_LIST+currentPosition)
        	max_friends = UPDATE_LIST+currentPosition;
        else
        	max_friends = max;
		u = new User[max_friends];
		for(int i = 0; i<max_friends; i++ ){
			currentPosition += 1;
			u[i]=friends.get(i);	
		}
		
		Log.d("Artem", currentPosition.toString());
    }
	
	/*private Handler updateListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UPDATE_LIST:
                adapter.notifyDataSetChanged();
                break;
            }
            ;
        };
    };*/

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d("Sc","Sc");
		boolean loadMore = /* maybe add a padding */
				(firstVisibleItem + visibleItemCount >= totalItemCount) && (firstVisibleItem + visibleItemCount <= max);
				if(loadMore) {
					Log.d("Sc","Sc2");
					addMoreDataToList(); // or any other amount
					adapter = new MySimpleArrayAdapterFast(this, u);
					listView.setAdapter(adapter);
					listView.setSelection(totalItemCount);
					//listView.set
		            adapter.notifyDataSetChanged();
		        }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void loadFriendDb(final ArrayList<User> friends_db) {
        new Thread(){
            @Override
            public void run(){
            	db_friends.addFriends(friends_db);
            	Pref.setLoadedFriendsDB(FriendsActivity.this);
            	db_friends.close();
            }
        }.start();
    }
	
	protected void checkExtras() {
		Intent i = getIntent();
		Bundle extras = getIntent().getExtras();
		if(i.hasExtra("f_msgs")){
			FORWARD_MESSAGES = true;
			forwarded_messages = extras.getString("f_msgs");
			Toast.makeText(getApplicationContext(), forwarded_messages.toString(), Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(getApplicationContext(), "Not forwarded messages", Toast.LENGTH_LONG).show();
		
		if(i.hasExtra("addUserToChat")){
			ADD_USER_TO_CHAT = true;
			Toast.makeText(getApplicationContext(), "ADD_USER_TO_CHAT", Toast.LENGTH_LONG).show();
		}
		
		if(i.hasExtra("inviteFriend")){
			INVITE_FRIEND = true;
			Toast.makeText(getApplicationContext(), "INVITE_FRIEND", Toast.LENGTH_LONG).show();
		}
	}
	
	public boolean onKeyDown(int keycode, KeyEvent event) {
	    if (keycode == KeyEvent.KEYCODE_BACK || keycode == KeyEvent.KEYCODE_HOME) {
	       INVITE_FRIEND = false;
	    }
	    return super.onKeyDown(keycode, event);
	}

}
