package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

import vk.adapters.OnlineFriendsAdapter;
import vk.api.API;
import vk.api.User;
import vk.db.datasource.FriendsDataSource;
import vk.pref.Pref;
import vk.utils.UserFullNameComparator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OnlineFriendsActivity extends Activity {
	private ArrayList<User> friends;
	private ArrayList<User> friends_db;
	private static int max;
	private static final int UPDATE_LIST = 100;
	private static Integer currentPosition = 0;
	private static User [] u;
	static ListView listView;
	OnlineFriendsAdapter adapter;
	private FriendsDataSource db_friends;
	
	private ArrayList<User> online_friends = new ArrayList();
	
	private static String lastLetter;
	
	private static AnimationDrawable loader;
	private static ImageView loader_photo;
	private static View headerView;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.online_friends);
   
	    final Handler handler = new Handler();
	    db_friends = new FriendsDataSource(this);
	    db_friends.open();
	    
	    listView = (ListView)findViewById(R.id.list);
	    listView.setOnItemClickListener(clickDilog);
	    
	    new AsyncTask<Context, Void, Void>() {
	    	
	    	@Override
        	protected void onPreExecute() {
	        	headerView = ((LayoutInflater)OnlineFriendsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader_header, null, false);

	    		listView.addHeaderView(headerView);
	    		
	    		loader_photo = (ImageView) headerView.findViewById(R.id.iv_header_loader);
	    		loader_photo.setBackgroundResource(R.anim.loader_blue);
	    		loader = (AnimationDrawable) loader_photo.getBackground();
	    		loader_photo.post(new Starter());

	    		listView.setAdapter(null);
	        }

	        @Override
	        protected Void doInBackground(Context... params) {
		            API api = new API(Pref.getAccessTokenHTTPS(OnlineFriendsActivity.this)); 
		            try {
						friends = api.getFriends(Pref.getUserID(OnlineFriendsActivity.this));
					} catch (Exception e) { e.printStackTrace(); } 

		            listView = (ListView)findViewById(R.id.list);
		            
		            u = new User[friends.size()];
		            
		            // сортируем оставшихся друзей
		            Collections.sort(friends, new UserFullNameComparator());
		            
		            // добавляем отсортированных друзей.
		            max = friends.size();
		            
		    		for(int i = 0; i<max; i++ ){
		    			if(friends.get(i).online)
		    				online_friends.add(friends.get(i));
		    		}		    	
		    		
		    		max = online_friends.size();
		    		u = new User[max];
		    		
		    		for(int i = 0; i<max; i++ ){
		    			u[i] = online_friends.get(i);
		    		}		   
		    		
		    		// добавляем разделители
		    		for(int i = 0; i<max; i++){
			    	        String letter = Character.toString(u[i].first_name.charAt(0)).toUpperCase();
			    	        if(!letter.equals(lastLetter)){
			    	        	OnlineFriendsAdapter.letter_buffer.put(i, letter);
			    			    lastLetter = letter;
			    	        }
		    		}		    		
		    		
	                return null;
	            } 
	        
	            @Override
	            public void onPostExecute(Void result){
	                handler.post(new Runnable(){
	                     @Override
	                     public void run(){
	     		            adapter = new OnlineFriendsAdapter(OnlineFriendsActivity.this, u);
	                        listView.removeHeaderView(headerView);
	                 		listView.setAdapter(adapter);
	                     }
	                });
	            }
	       }.execute();
	}
	
	class Starter implements Runnable {
		public void run() {
			loader.start();			
		}  	
    }
	
	class Stoper implements Runnable {
		public void run() {
			loader.stop();			
		}  	
    }
	
    private OnItemClickListener clickDilog=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			User friend = new User();
			friend = u[position];
			Intent intent = new Intent(OnlineFriendsActivity.this, DialogActivity.class);
			intent.putExtra("uid", friend.uid);
			intent.putExtra("type", "uid");
			startActivity(intent);
		}		
	};

}
