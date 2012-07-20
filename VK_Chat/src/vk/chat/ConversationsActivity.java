package vk.chat;

import java.util.ArrayList;

import vk.adapters.ConversationsAdapter;
import vk.adapters.MySimpleArrayAdapterFast;
import vk.adapters.SearchAdapter;
import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.constants.Constants;
import vk.db.datasource.ConversationsDataSource;
import vk.db.datasource.FriendsDataSource;
import vk.popup.ActionItem;
import vk.popup.QuickAction;
import vk.pref.Pref;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationsActivity extends Activity {
	private ArrayList<Message> dialogs;
	private ImageView ava;
	private ConversationsDataSource conversations;
	private static Message [] m;
	private static Message [] m2;
	private static ListView listView;
	private static ConversationsAdapter adapter;
	private static View headerView;
	
	private static AnimationDrawable loader;
	private static ImageView loader_photo;
	
	private EditText search;
	private ImageView search_icon;
	public static boolean inSearching = false;
	private final Handler handler = new Handler();
	
	private API api;
	
	private ArrayList<Message> search_list;
	private Message [] search_array;
	
	private boolean isSetHeader = false;
	boolean  running = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.conversations);
        
        search = (EditText) findViewById(R.id.iv_friends_search_editor);
		search.addTextChangedListener(searchListener);
        
		search_icon = (ImageView) findViewById(R.id.iv_search);
        
        conversations = new ConversationsDataSource(this);
        
        headerView = ((LayoutInflater)ConversationsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader_header, null, false);
        
        listView = (ListView)findViewById(R.id.list_dialogs);
//        listView.setOnItemLongClickListener (new OnItemLongClickListener() {
//        	  public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
//        		  deleteMessageDialog(position);
//        		  return false;
//        	  }
//        	});
        updater();
        updateList();
	    
        
        
        
        /*try{
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
		});*/
	}
	
	private void loadConversationsDb(final ArrayList<Message> conversations_db) {
        new Thread(){
            @Override
            public void run(){
            	conversations.addConversations(conversations_db);
            	Pref.setLoadedConversationsDB(ConversationsActivity.this);
            	conversations.close();
            }
        }.start();
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
				searchDialogs(search.getText().toString());				
			}
		}		
	};
	
	private void updateList(){
		new AsyncTask<Context, Void, Void>() {
	    	
	    	@Override
        	protected void onPreExecute() {
	        	
	        	/*if(isSetHeader){
	        		listView.removeHeaderView(headerView);
	        		listView.setAdapter(null);
	        		isSetHeader = true;
	        	}*/
	    		listView.setAdapter(null);
	        	listView.addHeaderView(headerView);
	    		
	    		loader_photo = (ImageView) headerView.findViewById(R.id.iv_header_loader);
	    		loader_photo.setBackgroundResource(R.anim.loader_blue);
	    		loader = (AnimationDrawable) loader_photo.getBackground();
	    		loader_photo.post(new Starter());

	    		listView.setAdapter(null);
	        }

	        @Override
	        protected Void doInBackground(Context... params) {
	        		conversations.open();
	        		api = new API(Pref.getAccessTokenHTTPS(ConversationsActivity.this));
		            
	        		Pref.cancelLoadedConversationsDB(ConversationsActivity.this);
	        		
		            if(!Pref.loadedConversationsDB(ConversationsActivity.this)){
		            	try{
		            		dialogs = api.getMessagesDialogs(0, 10);	    			
			    		} catch (Exception e){
			    			e.printStackTrace();
			    		} 
		            	conversations.removeAll();
		            	loadConversationsDb(dialogs);
		            }
		            else{		            	
		            	dialogs = conversations.getAllConversations();
		            	conversations.close();
		            }
		            Log.d("FriendsActivity.this", dialogs.toString());
	            	            
		            m = convertToArray(dialogs);
//		            m = new Message[dialogs.size()];
//		    		for(int i = 0; i<dialogs.size(); i++ )
//		    			m[i]=dialogs.get(i);
		              	
	                return null;
	            } 	        	
		        
	            @Override
	            public void onPostExecute(Void result){
	                handler.post(new Runnable(){
	                     @Override
	                     public void run(){
	                    	 
	                        adapter = new ConversationsAdapter(ConversationsActivity.this, m);
	                        listView.removeHeaderView(headerView);
	                        listView.setAdapter(null);
	                 		listView.setAdapter(adapter);
	                 		
	                 		listView.setOnItemClickListener(clickUser);
	                 		//loader_photo.post(new Stoper());	     			       
	                     }
	                });
	            }
	       }.execute();
	}

	private OnItemClickListener clickUser = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Message msg = new Message();
			msg = m[position];
			
			Intent intent = new Intent(ConversationsActivity.this, DialogActivity.class);
			if(msg.chat_id != null && msg.chat_id != 0){				
				Intent intent2 = new Intent(ConversationsActivity.this, ChatActivity.class);
				intent2.putExtra("chat_id", msg.chat_id);
				intent2.putExtra("type", "chat_id");
				intent2.putExtra("title", msg.title);
				startActivity(intent2);
			}
			else{					
				intent.putExtra("uid", msg.uid);
				intent.putExtra("type", "uid");
				startActivity(intent);
			}				
		}
	};
	
    private void searchDialogs(final String q) {
        new Thread(){
            @Override
            public void run(){            	
            	try{
	    			search_list = api.searchMessages(q);
	    		} catch (Exception e){
	    			e.printStackTrace();
	    		}
            	if(search_list != null)
            		m = convertToArray(search_list);
            	runOnUiThread(updateListSearch);
            }
        }.start();
    }
    
    Runnable updateListSearch=new Runnable(){
        @Override
        public void run() {
        	loader.stop();
        	search_icon.setBackgroundResource(R.drawable.search_icon);
        	adapter = new ConversationsAdapter(getApplicationContext(), m);  
        	listView.setAdapter(null);
	        listView.setAdapter(adapter);
        }
    };
    
    private Message[] convertToArray(ArrayList<Message> msgs){
		Message [] u = new Message[msgs.size()];
        
        for(int i = 0; i<msgs.size(); i++ )
			u[i]=msgs.get(i);
        Log.d("convertToArray ConversationActivity", msgs.toString());
		return u;
	}
    
    private void deleteMessageDialog(final int position){
    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	deleteDialog(position);
    	            break;

    	        case DialogInterface.BUTTON_NEGATIVE:   	        	
    	            break;
    	        }
    	    }
    	};

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(getResources().getString(R.string.sure))
    			.setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
    			.setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
    }
    
    private void deleteDialog(final int position) {
        new Thread(){
            @Override
            public void run(){      
            	int response=1;
            	try{
            		if(m[position].chat_id != null && m[position].chat_id != 0)	
            			response = api.deleteMessageThread(0L, m[position].chat_id);
            		else
            			response = api.deleteMessageThread(m[position].uid, 0L);
	    		} catch (Exception e){
	    			e.printStackTrace();
	    		}
            	if(response == 1)
            		runOnUiThread(updateList);
            	else
            		runOnUiThread(notSuccessDeleting);
 
            }
        }.start();
    }
    
    Runnable updateList=new Runnable(){
        @Override
        public void run() {
        	updateList();
        	Toast.makeText(getApplicationContext(),
        			getResources().getString(R.string.dialog_deleting_success), Toast.LENGTH_LONG)
					.show();
        }
    };
    
    Runnable notSuccessDeleting=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(getApplicationContext(),
        			getResources().getString(R.string.dialog_deleting_success), Toast.LENGTH_LONG)
					.show();
        }
    };
    
    private void updater() {
        new Thread(){
            @Override
            public void run(){
            	while(running){
            		if(VK_ChatActivity.needUpdateConv){
            			runOnUiThread(updateList);
            			VK_ChatActivity.needUpdateConv = false;
            		}          
                	Log.d("updater Convers", "updater convers");
                	try {
        				Thread.sleep(500);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			} 
            	}
            }
        }.start();
    }
    
    public boolean onKeyDown(int keycode, KeyEvent event) {
	    if (keycode == KeyEvent.KEYCODE_BACK || keycode == KeyEvent.KEYCODE_HOME) {
	    	running = false;
	    }
	    return super.onKeyDown(keycode, event);
	}
    
    @Override
    protected void onStop() {
    	//running = false;
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	running = false;
    	super.onDestroy();
    }
    
    @Override
    protected void onRestart() {
    	running = true;
    	updater();
    	super.onRestart();
    }
}
