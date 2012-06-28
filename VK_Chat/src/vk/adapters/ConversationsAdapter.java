package vk.adapters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.chat.ChatActivity;
import vk.chat.ConversationsActivity;
import vk.chat.FriendsActivity;
import vk.chat.R;
import vk.chat.R.drawable;
import vk.chat.R.id;
import vk.chat.R.layout;
import vk.constants.Constants;
import vk.db.datasource.ChatDataSource;
import vk.db.datasource.FriendsDataSource;
import vk.pref.Pref;
import vk.utils.WorkWithTimeAndDate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationsAdapter extends ArrayAdapter<Message> {	
    private final Context context;
    private final Message[] values;
    public static TextView text;
    public static TextView image;
    public static LayoutInflater inflater;
    public ImageDownloader loader;
    public API api;
    public ArrayList<User> chat_users = new ArrayList();
    private ChatDataSource db_chatUsers;
    ViewHolderChat viewHolderChat = new ViewHolderChat();
    
    
    
    static class ViewHolder {
		public TextView text;
		public ImageView image_ava;
		public ImageView image_online;
		public TextView time;
	}
    
    static class ViewHolderChat{
    	public TextView text;
		public ImageView image_ava1;
		public ImageView image_ava2;
		public ImageView image_ava3;
		public ImageView image_ava4;		
    }

    public ConversationsAdapter(Context context, Message[] objects) {
        super(context, R.layout.dialog_list, objects);
        this.context = context;
        this.values = objects;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loader =  new ImageDownloader();
        api = new API(Pref.getAccessTokenHTTPS(context));
        //db
        db_chatUsers = new ChatDataSource(context);
        
    }
    
    public int getItemViewType(int position){
    	if(values[position].chat_id != null && values[position].chat_id != 0)
    		return 2;
    	return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	int viewType = this.getItemViewType(position);
    	
    	switch(viewType){
	    	case 1:
	    		ViewHolder viewHolder = new ViewHolder();
		        //if((convertView == null)){
		        	convertView = inflater.inflate(R.layout.dialog_list, parent, false);
		        	
					viewHolder.text = (TextView) convertView.findViewById(R.id.iv_conv_last_message);
					viewHolder.image_ava = (ImageView) convertView.findViewById(R.id.iv_conv_avatar);
					viewHolder.image_online = (ImageView) convertView.findViewById(R.id.iv_conv_online);
					viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
					convertView.setTag(viewHolder);
				//} else {
				//		viewHolder = (ViewHolder) convertView.getTag();
				//}
		        
		        viewHolder.text.setText(values[position].body);
		        viewHolder.image_ava.setImageResource(R.drawable.contact_nophoto);
		        viewHolder.time.setText(WorkWithTimeAndDate.getTime(values[position].date, context));
		        viewHolder.image_online.setBackgroundResource(R.drawable.online_list);
		        viewHolder.image_ava.setTag(values[position]);
		        //loader.download(chat_users.get(0).photo_rec, viewHolderChat.image_ava1);
		        /*Log.d("Online",values[position].online.toString());
		        if(values[position].online == true){
		        	viewHolder.image_online.setBackgroundResource(R.drawable.online_list);
		        }
		        else{
		        	viewHolder.image_online.setBackgroundColor(Color.BLACK);
		        }
		        viewHolder.image_ava.setTag(values[position].photo_rec);
		        loader.download(values[position].photo_rec, viewHolder.image_ava);*/
		        return convertView;
	    	case 2:
	    		
		        //if(v2 == null || v2.getTag() == v){
	    		convertView = inflater.inflate(R.layout.chat_list, parent, false);
					//viewHolder.text = (TextView) convertView.findViewById(R.id.iv_conv_last_message);
					viewHolderChat.image_ava1 = (ImageView) convertView.findViewById(R.id.iv_ava_1);
					viewHolderChat.image_ava2 = (ImageView) convertView.findViewById(R.id.iv_ava_2);
					viewHolderChat.image_ava3 = (ImageView) convertView.findViewById(R.id.iv_ava_3);
					viewHolderChat.image_ava4 = (ImageView) convertView.findViewById(R.id.iv_ava_4);
					convertView.setTag(viewHolderChat);
					
					convertView.post(new Runnable(){
				        @Override
				        public void run() {
				        	try {
		    					chat_users = api.getChatUsers(values[position].chat_id);
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		                viewHolderChat.image_ava1.setTag(chat_users.get(0).photo_rec);
				        loader.download(chat_users.get(0).photo_rec, viewHolderChat.image_ava1);
				        viewHolderChat.image_ava2.setTag(chat_users.get(1).photo_rec);
				        loader.download(chat_users.get(1).photo_rec, viewHolderChat.image_ava2);
				        viewHolderChat.image_ava3.setTag(chat_users.get(2).photo_rec);
				        loader.download(chat_users.get(2).photo_rec, viewHolderChat.image_ava3);
				        viewHolderChat.image_ava4.setTag(chat_users.get(3).photo_rec);
				        loader.download(chat_users.get(3).photo_rec, viewHolderChat.image_ava4);
				        notifyDataSetChanged();
				        }
				    });
									
				//} else {
				//		viewHolderChat = (ViewHolderChat) convertView.getTag();
				//}
				
					//Log.d("chat_id",values[position].chat_id.toString());
					
					//Pref.cancelLoadedChatDB(context);
					/*db_chatUsers.open();
					
					if(!Pref.loadedChatDB(context)){
		            	try{
		            		chat_users = api.getChatUsers(values[position].chat_id);		    			
			    		} catch (Exception e){
			    			e.printStackTrace();
			    		}
		            	db_chatUsers.removeAll();  
		            	//Log.d("db_chat","");
		            	loadChatDb(chat_users, values[position].chat_id);
		            }
		            else{
		            	chat_users = db_chatUsers.getChatUsers(values[position].chat_id);
		            	//Log.d("-------",chat_users.toString());
		            	db_chatUsers.close();
		            }

					//Log.d("photo1", chat_users.get(0).photo_rec);
					//checkPhone(position);
					if(chat_users != null){
						
					}*/
		        /*Log.d("Online",values[position].online.toString());
		        if(values[position].online == true){
		        	viewHolder.image_online.setBackgroundResource(R.drawable.online_list);
		        }
		        else{
		        	viewHolder.image_online.setBackgroundColor(Color.BLACK);
		        }
		        viewHolder.image_ava.setTag(values[position].photo_rec);
		        loader.download(values[position].photo_rec, viewHolder.image_ava);*/
		        return convertView;
	    	}
    	return null;
    }
    
    
    private void checkPhone(final int position) {
        new Thread(){
            @Override
            public void run(){
                
            }
        }.start();
    }
    
    Runnable checkPhoneOkRunnable=new Runnable(){
        @Override
        public void run() {

        }
    };
    
	private void loadChatDb(final ArrayList<User> friends_db, final Long chat_id) {
        new Thread(){
            @Override
            public void run(){
            	Log.d("loaded",friends_db.toString());
            	db_chatUsers.addChatUsers(friends_db, chat_id);
            	Pref.setLoadedChatDB(context);
            	db_chatUsers.close();
            }
        }.start();
    }
    
}