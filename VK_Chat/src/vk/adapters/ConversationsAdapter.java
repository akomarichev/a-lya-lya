package vk.adapters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.chat.ChatActivity;
import vk.chat.R;
import vk.chat.R.drawable;
import vk.chat.R.id;
import vk.chat.R.layout;
import vk.constants.Constants;
import vk.pref.Pref;

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
    
    static class ViewHolder {
		public TextView text;
		public ImageView image_ava;
		public ImageView image_online;
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
    }
    
    public int getItemViewType(int position){
    	if(values[position].chat_id != null)
    		return 2;
    	return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	int viewType = this.getItemViewType(position);
    	
    	switch(viewType){
	    	case 1:
		    	ViewHolder viewHolder = new ViewHolder();
		        if(convertView == null){
		        	convertView = inflater.inflate(R.layout.dialog_list, null);
					viewHolder.text = (TextView) convertView.findViewById(R.id.iv_conv_last_message);
					viewHolder.image_ava = (ImageView) convertView.findViewById(R.id.iv_conv_avatar);
					viewHolder.image_online = (ImageView) convertView.findViewById(R.id.iv_conv_online);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
		        
		        viewHolder.text.setText(values[position].body);
		        viewHolder.image_ava.setBackgroundResource(R.drawable.contact_nophoto);
		        viewHolder.image_online.setBackgroundResource(R.drawable.online_list);
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
	    		ViewHolderChat viewHolderChat = new ViewHolderChat();
		        if(convertView == null){
		        	convertView = inflater.inflate(R.layout.chat_list, null);
					//viewHolder.text = (TextView) convertView.findViewById(R.id.iv_conv_last_message);
					viewHolderChat.image_ava1 = (ImageView) convertView.findViewById(R.id.iv_ava_1);
					viewHolderChat.image_ava2 = (ImageView) convertView.findViewById(R.id.iv_ava_2);
					viewHolderChat.image_ava3 = (ImageView) convertView.findViewById(R.id.iv_ava_3);
					viewHolderChat.image_ava4 = (ImageView) convertView.findViewById(R.id.iv_ava_4);
					convertView.setTag(viewHolderChat);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
		        
				try {
					chat_users = api.getChatUsers(values[position].chat_id);
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
		        
		        viewHolderChat.image_ava1.setTag(chat_users.get(0).photo_rec);
		        loader.download(chat_users.get(0).photo_rec, viewHolderChat.image_ava1);
		        viewHolderChat.image_ava2.setTag(chat_users.get(0).photo_rec);
		        loader.download(chat_users.get(1).photo_rec, viewHolderChat.image_ava2);
		        viewHolderChat.image_ava3.setTag(chat_users.get(0).photo_rec);
		        loader.download(chat_users.get(2).photo_rec, viewHolderChat.image_ava3);
		        viewHolderChat.image_ava4.setTag(chat_users.get(0).photo_rec);
		        loader.download(chat_users.get(3).photo_rec, viewHolderChat.image_ava4);
		        
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
    
    
    
    
}