package vk.chat;

import java.util.ArrayList;
import java.util.Collections;

import vk.adapters.MySimpleArrayAdapterFast;
import vk.adapters.SearchAdapter;
import vk.api.API;
import vk.api.User;
import vk.chat.ConversationsActivity.Starter;
import vk.pref.Pref;
import vk.utils.UserFullNameComparator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	
	public static final int REQUEST = 1;
	public static final int SUGGESTION = 2;
	
	private static ListView listView;
	private EditText search;
	private ImageView search_icon;
	private Handler handler;
	private Long [] request_friends;
	private ArrayList<User> request_friends_array_list;
	private User [] request_friends_array;
	private ArrayList<User> suggestions_list;
	private User [] suggestions_array;
	private User [] mutual;
	private ArrayList<User> search_list;
	private User [] search_array;
	public static int separator = 0;
	public static boolean inSearching = false;
	
	private View header;
	private View header_loader;
	private static AnimationDrawable loader;
	private static ImageView loader_photo;
	private boolean isSetHeader = false;
	
	private SearchAdapter adapter;
	private API api;
	public static boolean requests = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.search);
	    
	    handler = new Handler();
	    setupUI();
	    
	    updateList();
	}
	
	void setupUI(){
		search = (EditText) findViewById(R.id.iv_friends_search_editor);
		search.addTextChangedListener(searchListener);
		
		search_icon = (ImageView) findViewById(R.id.iv_search);
		header = ((LayoutInflater)SearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.search_header, null, false);
		header_loader = ((LayoutInflater)SearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader_header, null, false);
		
		listView = (ListView)findViewById(R.id.list_friends);
	    listView.setOnItemClickListener(clickUser);
	}
	
	private TextWatcher searchListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			if(search.getText().toString().equals("")){
				inSearching = false;
				updateList();				
			}
			else{
				search_icon.setBackgroundResource(R.anim.loader_grey);
				loader = (AnimationDrawable) search_icon.getBackground();
				search_icon.setClickable(false);
				loader.start();
				inSearching = true;
				searchUsers(search.getText().toString());				
			}
		}		
	};
	
	private OnItemClickListener clickUser=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			if((position <= separator || separator == 0) && !inSearching && position != 0 && requests){
				Intent intent = new Intent(SearchActivity.this, RequestActivity.class);
				intent.putExtra("type", REQUEST);
				intent.putExtra("name", mutual[position-1].first_name + " " + mutual[position-1].last_name);
				intent.putExtra("photo", mutual[position-1].photo_medium);
				intent.putExtra("uid", mutual[position-1].uid);
		        startActivityForResult(intent, REQUEST);
			}
			else if(position > separator && !requests){
				Intent intent = new Intent(SearchActivity.this, RequestActivity.class);
				intent.putExtra("type", SUGGESTION);
				intent.putExtra("name", mutual[position-1].first_name + " " + mutual[position-1].last_name);
				intent.putExtra("photo", mutual[position-1].photo_medium);
				intent.putExtra("uid", mutual[position-1].uid);
		        startActivityForResult(intent, SUGGESTION);
			}
		}		
	};
	
	private void updateList(){
		new AsyncTask<Context, Void, Void>() {    	
	    	@Override
        	protected void onPreExecute() {
	    		if(isSetHeader){
	    			listView.removeHeaderView(header);
        			listView.setAdapter(null);        			
	    		}
	    		
	    		listView.addHeaderView(header_loader);
	    		isSetHeader = true;
	    		
	    		loader_photo = (ImageView) header_loader.findViewById(R.id.iv_header_loader);
	    		loader_photo.setBackgroundResource(R.anim.loader_blue);
	    		loader = (AnimationDrawable) loader_photo.getBackground();
	    		loader_photo.post(new Starter());
	    		listView.setAdapter(null);
	        }

	        @Override
	        protected Void doInBackground(Context... params) {
		            api = new API(Pref.getAccessTokenHTTPS(SearchActivity.this)); 
		            try{
		    			request_friends = api.getRequestsFriends();	    	
		    			if(request_friends!=null){
		    				request_friends_array_list = api.getUsers(convertToString(request_friends));
		    				request_friends_array = convertToArray(request_friends_array_list);
		    			}
		    		} catch (Exception e){
		    			e.printStackTrace();
		    		}
		            
		            Log.d("request_friends", request_friends+"");
	                return null;
	            } 
	        
	        @Override
	        public void onPostExecute(Void result){
	        	handler.post(new Runnable(){
	        		@Override
	                public void run(){
	        			listView.removeHeaderView(header_loader);
	        			listView.setAdapter(null);
	        			isSetHeader = false;
	                    if(!isSetHeader){
	                    	listView.addHeaderView(header);
	                    	isSetHeader = true;
	                    }
	                    if(request_friends_array == null)
	                    	listView.setAdapter(null);
	                    else{
	                    	adapter = new SearchAdapter(getApplicationContext(), request_friends_array);
	                    	listView.setAdapter(adapter);
	                    }
     		            loadSuggestions();
	        		}
	        	});
	        }
	    }.execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	    if(data.hasExtra("need_update"))
	    	updateList();
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
	
	private String convertToString(Long[] users){
		String usrs = "";
		for(int i = 0; i<users.length; i++)                		
    		usrs += users[i] + ",";     
		usrs = usrs.substring(0, usrs.length() - 1);
		Log.d("convertToString", usrs);
		return usrs;
	}
	
	private User[] convertToArray(ArrayList<User> users){
		User [] u = new User[users.size()];
        
        for(int i = 0; i<users.size(); i++ )
			u[i]=users.get(i);
        Log.d("convertToArray", users.toString());
		return u;
	}
	
	private User[] join(User[] request, User[] suggestion){
		int l1, l2;
		if(request != null){
			l1 = request.length;
			requests = true;
		}
		else{
			l1 = 0;
			requests = false;
		}
		if(suggestion != null)
			l2 =suggestion.length;
		else
			l2 = 0;
		User[]mutual = new User[l1+l2];
		int i = 0;
		for(; i<l1; i++)
			mutual[i] = request[i];
		separator = i;
		for(int j = 0; j<l2; j++)
			mutual[i+j] = suggestion[j];
		return mutual;
	}
	
	private void loadSuggestions() {
        new Thread(){
            @Override
            public void run(){
            	try{
	    			suggestions_list = api.getSuggestions();
	    		} catch (Exception e){
	    			e.printStackTrace();
	    		}
            	suggestions_array = convertToArray(suggestions_list);
            	mutual = SearchActivity.this.join(request_friends_array, suggestions_array);
            	runOnUiThread(updateList);
            }
        }.start();
    }
	
	Runnable updateList=new Runnable(){
        @Override
        public void run() {
        	adapter = new SearchAdapter(getApplicationContext(), mutual);
	        listView.setAdapter(adapter);
        }
    };
    
    private void searchUsers(final String q) {
        new Thread(){
            @Override
            public void run(){            	
            	try{
	    			search_list = api.searchUser(q);
	    		} catch (Exception e){
	    			e.printStackTrace();
	    		}
            	if(search_list != null)
            		mutual = convertToArray(search_list);
            	runOnUiThread(updateListSearch);
            }
        }.start();
    }
    
    Runnable updateListSearch=new Runnable(){
        @Override
        public void run() {
        	loader.stop();
        	search_icon.setBackgroundResource(R.drawable.search_icon);
        	adapter = new SearchAdapter(getApplicationContext(), mutual);        	
	        listView.setAdapter(adapter);
        }
    };
	
	
}
