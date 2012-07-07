package vk.chat;

import java.io.IOException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;

import vk.adapters.HandsetContactsAdapter;
import vk.adapters.MySimpleArrayAdapterFast;
import vk.adapters.SearchAdapter;
import vk.api.API;
import vk.api.User;
import vk.chat.SearchActivity.Starter;
import vk.pref.Pref;
import vk.utils.Synch;
import vk.utils.UserFullNameComparator;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HandsetContactsActivity extends Activity{
	private static ListView listView;
	private EditText search;
	private ImageView search_icon;
	private Handler handler;
	
	public static boolean inSearching = false;
	
	private View header_synch;
	private View header_loader;
	private static AnimationDrawable loader;
	private static ImageView loader_photo;
	private boolean isSetHeader = false;
	
	private HandsetContactsAdapter adapter;
	private API api;
	
	private Button b_synch; 
	private RelativeLayout header_search;
	
	private Map<String,String> list_phone;
	
	private String phone = "";
	private ArrayList<User> users_list;
	private User[] users_array;
	
	private static String lastLetter;
	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.synch);
		
		Pref.deleteSynch(this);
		api = new API(Pref.getAccessTokenHTTPS(HandsetContactsActivity.this));
		handler = new Handler();
	    setupUI();
		
	}
	
	void setupUI(){
		search = (EditText) findViewById(R.id.iv_friends_search_editor);
		search.addTextChangedListener(searchListener);
		
		search_icon = (ImageView) findViewById(R.id.iv_search);
		header_search = (RelativeLayout) findViewById(R.id.header_synch);
		
		header_loader = ((LayoutInflater)HandsetContactsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader_header, null, false);
		
		listView = (ListView)findViewById(R.id.list_friends);
	    listView.setOnItemClickListener(clickUser);
	    
	    setupHeaderSynch();
	}
	
	void setupHeaderSynch(){
		header_synch = ((LayoutInflater)HandsetContactsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.synch_contacts, null, false);
		b_synch = (Button) header_synch.findViewById(R.id.b_synch_cont);
		b_synch.setOnClickListener(clickSynch);
		listView.addHeaderView(header_synch);
		listView.setAdapter(null);
		header_search.setVisibility(View.GONE);
		isSetHeader=true;
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
				//updateList();				
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
	
	private void searchUsers(final String q) {
        new Thread(){
            @Override
            public void run(){            	
            	try{
	    			//search_list = api.searchUser(q);
	    		} catch (Exception e){
	    			e.printStackTrace();
	    		}
            	//if(search_list != null)
            		//mutual = convertToArray(search_list);
            	runOnUiThread(updateListSearch);
            }
        }.start();
    }
    
    Runnable updateListSearch=new Runnable(){
        @Override
        public void run() {
        	loader.stop();
        	search_icon.setBackgroundResource(R.drawable.search_icon);
        	adapter = new HandsetContactsAdapter(getApplicationContext(), users_array, list_phone);        	
	        listView.setAdapter(adapter);
        }
    };
    
    private OnItemClickListener clickUser=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			if(position == 0){
				Intent intent = new Intent(HandsetContactsActivity.this, FriendsActivity.class);
				intent.putExtra("inviteFriend", "inviteFriend");
		        startActivity(intent);
			}
			if(position > 0){
				if(isAppUser(users_array[position].uid)){
					Intent intent = new Intent(HandsetContactsActivity.this, UserRegisteredActivity.class);
					intent.putExtra("name", users_array[position].first_name + " " + users_array[position].last_name);
					intent.putExtra("photo", users_array[position].photo_medium);
					intent.putExtra("uid", users_array[position].uid);
					intent.putExtra("phone", users_array[position].mobile_phone);
					startActivity(intent);
				}
				else{
					Intent intent = new Intent(HandsetContactsActivity.this, UserNotRegisteredActivity.class);
					intent.putExtra("name", users_array[position].first_name + " " + users_array[position].last_name);
					intent.putExtra("photo", users_array[position].photo_medium);
					intent.putExtra("uid", users_array[position].uid);
					intent.putExtra("phone", users_array[position].mobile_phone);
					startActivity(intent);
				}
			}
		}		
	};
	
	private OnClickListener clickSynch=new OnClickListener(){
        @Override
        public void onClick(View v) {
        	//VK_ChatActivity.iv_notify_mess.setVisibility(View.INVISIBLE);
        	//VK_ChatActivity.iv_notify_search.setVisibility(View.INVISIBLE);
        	//VK_ChatActivity.tv_notify_search.setText("20");
        	updateList();
        }
    };
    
    private void getPhoneList(){
    	list_phone = new HashMap();
    	Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
          String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));          
          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          if(phoneNumber.length() >= 11 && phoneNumber.length() <= 12){
        	  phone += phoneNumber+",";
        	  list_phone.put(phoneNumber, name);
          }
        }
        phones.close();
        phone = phone.substring(0, phone.length() - 1);
    }
    
	private void updateList(){
		new AsyncTask<Context, Void, Void>() {    	
	    	@Override
        	protected void onPreExecute() {
	    		if(isSetHeader){
	    			listView.removeHeaderView(header_synch);
	    			header_search.setVisibility(View.VISIBLE);
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
	        	getPhoneList();
		            try{
		            	users_list = api.getUsersByPhones(phone);
		    		} catch (Exception e){
		    			e.printStackTrace();
		    		}
		            
		            Collections.sort(users_list, new UserFullNameComparator());
		            
		            users_array = new User[users_list.size()+1];
		            users_array[0] = new User();
		            //users_array[0] = users_list.get(0);
		    		for(int i = 1; i<=users_list.size(); i++ ){
		    			users_array[i]=users_list.get(i-1);	
		    		}
		    		
		    		for(int i = 1; i<users_array.length; i++){
			    	        String letter = Character.toString(users_array[i].first_name.charAt(0)).toUpperCase();
			    	        if(!letter.equals(lastLetter)){
			    	        	HandsetContactsAdapter.letter_buffer.put(i, letter);
			    			    lastLetter = letter;
			    	        }
		    		}
		    		
		            //Log.d("request_friends", request_friends+"");
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
	                    adapter = new HandsetContactsAdapter(getApplicationContext(), users_array, list_phone);
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
	
	private boolean isAppUser(Long uid){
		Long result = 0L;
		try {
			result = api.isAppUser(uid);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result == 1L;
	}
}















































		
//		SharedPreferences prefs = getSharedPreferences("Synch", MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
//		final Editor editor=prefs.edit();
//		press_synch = prefs.getBoolean("press_synch", false);		
//		
//		if(press_synch){
//			replaceContentView("SynchActivity", new Intent(this, SynchActivity.class));
//		}
//		else{
//			launchButton = (Button)findViewById(R.id.b_synch_cont);
//			launchButton.setOnClickListener(new View.OnClickListener(){
//				public void onClick(View v) {
//					editor.putBoolean("press_synch", true).commit();
//					Intent activity3Intent = new Intent(v.getContext(), SynchActivity.class);
//					replaceContentView("activity3", activity3Intent);					
//					}
//			});
//		}

		
//	public void replaceContentView(String id, Intent newIntent) {			
//		View view = getLocalActivityManager().startActivity(id,newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)) .getDecorView(); 			
//		this.setContentView(view);
//			}        
//	}
/*public class HandsetContactsActivity extends Activity {
	private ArrayList<User> users = new ArrayList();
	private static User [] u;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    TextView textview = new TextView(this);
        textview.setText("This is the Handset Contacts tab");
        setContentView(textview);
        
        String phone = "";
        
        Map<String,String> list_phone = new HashMap();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
          String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));          
          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          if(phoneNumber.length() >= 11 && phoneNumber.length() <= 12){
        	  phone += phoneNumber+",";
        	  list_phone.put(phoneNumber, name);
          }
        }
        phones.close();
        phone = phone.substring(0, phone.length() - 1);
        
        /*for (Entry<String, String> entry : list_phone.entrySet()) {
            Log.d("key",entry.getKey());
            Log.d("value",entry.getValue());
        }
        Log.d("phone",phone);
        API api = new API();
        try {
			users = api.getUsersByPhones(phone);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	    
	    u = new User[users.size()];
		for(int i = 0; i<users.size(); i++ ){
			u[i]=users.get(i);	
		}
		
		Log.d("phone_users",u.toString());
        
		
	}
}*/
