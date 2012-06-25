package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import vk.adapters.ConversationsAdapter;
import vk.adapters.DialogAdapter;
import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.constants.Constants;
import vk.horizontal.listview.ui.HorizontalListView;
import vk.popup.ActionItem;
import vk.popup.QuickAction;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DialogActivity extends Activity {
	
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO   = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	
	private int max_attaches = 3;
	private int delete_position = -1;
	

	private long user_id;
	private long chat_id;
	private String type;
	private ArrayList<Integer> checkedItems = new ArrayList();
	//private ArrayList<User> chat_users =new ArrayList();
	private ArrayList<User> chat_users = new ArrayList();
	private HorizontalListView  lv_horizontal;
	
	private MyListAdapter myListAdapter;
	private ArrayList<String> items;
	
	private ImageView conv_attach;
	
	
	private ArrayList<Message> dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        
        conv_attach = (ImageView) findViewById(R.id.conv_attach);
        
        
        lv_horizontal  = (HorizontalListView) findViewById(R.id.lv_horizontal);
        items = new ArrayList();
        myListAdapter = new MyListAdapter(this, items);
        lv_horizontal.setAdapter(myListAdapter);
        //LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //lv_horizontal.setLayoutParams(lp);
        //lv_horizontal.setVisibility(View.INVISIBLE);
        
        lv_horizontal.setVisibility(View.GONE);
        
        ActionItem nextItem 	= new ActionItem(ID_OK, "Next", getResources().getDrawable(R.drawable.attach_photo_lable));
		ActionItem prevItem 	= new ActionItem(ID_ERASE, "Prev", getResources().getDrawable(R.drawable.attach_location_lable));
		ActionItem infoItem 	= new ActionItem(ID_INFO, "Info", getResources().getDrawable(R.drawable.menu_info));
        
		
		//prevItem.setSticky(true);
        //nextItem.setSticky(true);
        //infoItem.setSticky(true);
        
        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(infoItem);
		
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
                
		        items.add("date0");
		        items.add("date1");
		        items.add("date2");
		        items.add("date3");
		        
		        lv_horizontal.setVisibility(View.VISIBLE);
		        conv_attach.setBackgroundResource(R.drawable.attach);
		        //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
		        //lv_horizontal.startAnimation(hyperspaceJumpAnimation);
		        
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					Toast.makeText(getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_INFO) {
					Toast.makeText(getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		conv_attach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				conv_attach.setBackgroundResource(R.drawable.attach_pressed);
				quickAction.show(v);				
			}
		});
		
		Log.d("DialogActivity", "2");
        
		user_id = chat_id = 0;
		API api = new API(Pref.getAccessTokenHTTPS(DialogActivity.this));
		
		type = getIntent().getExtras().getString("type");
		if(type.equals("uid"))
			user_id = getIntent().getExtras().getLong("uid");
		else{
			chat_id = getIntent().getExtras().getLong("chat_id");
			try {
				chat_users = api.getChatUsers(chat_id);
				Log.d("chat_users",chat_users.toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
        
        try{
        	if(chat_id != 0)
        		dialog = api.getMessagesHistory(0, chat_id, (long) 0, 100);
        	else
        		dialog = api.getMessagesHistory(user_id, -20, (long) 0, 100);		
		} catch (Exception e){
			e.printStackTrace();
		} 
        
        Message m[] = new Message[dialog.size()];
		for(int i = 0; i<dialog.size(); i++ )
			m[dialog.size()-i-1]=dialog.get(i);	
        
		ListView listView = (ListView)findViewById(R.id.list_dialog);
        final DialogAdapter adapter = new DialogAdapter(this, m, checkedItems);
		listView.setAdapter(adapter);
		listView.setSelection(dialog.size()-1);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				checkedItems.add(position);
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private class MyListAdapter extends BaseAdapter {  
		
		private Context context;
	    private ArrayList<String> items;
	    
	    public MyListAdapter(Context context, ArrayList<String> items) {
	        this.context = context;
	        this.items = items;
	    }
		  
        @Override  
        public int getCount() {
            return items.size()+3;
        } 
  
        @Override  
        public Object getItem(int position) {  
        	return items.get(position); 
        }  
  
        @Override  
        public long getItemId(int position) {  
        	return position; 
        }  
  
        @Override  
        public View getView(final int position, View convertView, ViewGroup parent) {  
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);    
            ImageView title = (ImageView) retval.findViewById(R.id.image);
            ImageView delete = (ImageView) retval.findViewById(R.id.iv_delete_button);
            delete.setVisibility(View.GONE);
            if(position==getCount()-1){
            	title.setBackgroundResource(R.drawable.horizontal_gallery);
            	return retval;
            }
            if(position==getCount()-2){
            	title.setBackgroundResource(R.drawable.horizontal_geo);
            	return retval;
            }
            if(position==getCount()-3){
            	title.setBackgroundResource(R.drawable.horizontal_photo);
            	return retval;
            }
            /*if(position==getCount()-5){
            	title.setBackgroundResource(R.drawable.icon);
            	delete.setVisibility(View.VISIBLE);
            	delete.setOnClickListener(new MyClickListener(position));*/
                /*delete.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    		items.remove(position-3);
                    		notifyDataSetChanged();
                    }
                }
                );
            	return retval;
            }*/
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new MyClickListener(position));
            /*delete.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                		items.remove(position-3);
                		notifyDataSetChanged();
                }
            }
            );*/
            title.setBackgroundResource(R.drawable.contact_nophoto);
            return retval;
        }   
        
        private class MyClickListener implements OnClickListener {

            private int position;

            public MyClickListener(int position) {
               this.position = position;
            }

            public void onClick(View v) {
            	Toast.makeText(getApplicationContext(), "position: " + position + ", image = " + items.get(position), Toast.LENGTH_SHORT).show();
            	items.remove(position);
            	if(items.size()==0){
            		lv_horizontal.setVisibility(View.GONE);
            		conv_attach.setBackgroundResource(R.drawable.attach);
            	}
        		notifyDataSetChanged();
            }

            public int getPosition() {
              return position;
            }

         }
    }; 
}
