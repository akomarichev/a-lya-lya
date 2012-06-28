package vk.chat;

import java.util.ArrayList;

import vk.adapters.MySimpleArrayAdapterFast;
import vk.api.API;
import vk.api.User;
import vk.constants.Constants;
import vk.db.datasource.FriendsDataSource;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	private ArrayList<User> friends;
	private ArrayList<User> friends_db;
	private static int max;
	private static final int UPDATE_LIST = 100;
	private static Integer currentPosition = 0;
	private static User [] u;
	static ListView listView;
	MySimpleArrayAdapterFast adapter;
	private FriendsDataSource db_friends;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.friends);
   
	    final Handler handler = new Handler();
	    db_friends = new FriendsDataSource(this);
	    db_friends.open();
	    
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
		            	loadFriendDb(friends);
		            }
		            else{
		            	friends = db_friends.getAllFriends();
		            	db_friends.close();
		            }
		            	
		            Log.d("FriendsActivity.this", friends.toString());
	            	            
		            max = friends.size();
		            
		           	
		            listView = (ListView)findViewById(R.id.list);
		            //u = new User[friends.size()];
		            /*Integer max_friends;
		            if(max > UPDATE_LIST)
		            	max_friends = UPDATE_LIST;
		            else
		            	max_friends = max;*/
		            
		            u = new User[max];
		    		for(int i = 0; i<max; i++ ){
		    			//currentPosition += 1;
		    			u[i]=friends.get(i);	
		    		}		    	
	                return null;
	            } 
	        
	            @Override
	            public void onPostExecute(Void result){
	                handler.post(new Runnable(){
	                     @Override
	                     public void run(){
	     		             Log.d("Friends", u.toString());
	     		            
	     		            adapter = new MySimpleArrayAdapterFast(getApplicationContext(), u);
	     				   listView.setAdapter(adapter);
	     				   
	     			       //adapter.notifyDataSetChanged();
	     			       
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

}
