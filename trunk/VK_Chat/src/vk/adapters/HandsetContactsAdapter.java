package vk.adapters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vk.api.User;
import vk.chat.R;
import vk.chat.R.drawable;
import vk.chat.R.id;
import vk.chat.R.layout;
import vk.chat.SearchActivity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class HandsetContactsAdapter extends ArrayAdapter<User> {	
    private final Context context;
    private final User[] values;
    //private static TextView text;
    private static TextView image;
    private static LayoutInflater inflater;
    private ImageDownloader loader;
    
    public static HashMap<Integer,String> letter_buffer = new HashMap<Integer, String>();
    
    Map<String,String> list_phone;
    
    static class ViewHolder {
		public TextView name_vk;
		public TextView name_phone;
		public ImageView image_ava;
		
		public TextView letter;
		public RelativeLayout separator;
		public ImageView image_separator;
	}

    public HandsetContactsAdapter(Context context, User[] objects, Map<String,String> list_phone) {
        super(context, R.layout.synch_list_separator, objects);
        this.context = context;
        this.values = objects;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list_phone = list_phone;
        //loader =  new ImageDownloader();
        loader =  new ImageDownloader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.synch_list_separator, null);
			viewHolder.name_vk = (TextView) convertView.findViewById(R.id.tvNameFriend);
			viewHolder.name_phone = (TextView) convertView.findViewById(R.id.tvNamePhone);
			viewHolder.image_ava = (ImageView) convertView.findViewById(R.id.ivAvatarFriend);
			
			viewHolder.separator = (RelativeLayout) convertView.findViewById(R.id.layout_separator);
			viewHolder.image_separator = (ImageView) convertView.findViewById(R.id.image_separator);
			viewHolder.letter = (TextView) convertView.findViewById(R.id.letter);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        
        viewHolder.separator.setVisibility(View.GONE);
        LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if(position == 0){        	
        	viewHolder.image_ava.setVisibility(View.GONE);
        	viewHolder.name_phone.setVisibility(View.GONE);        	
        	params.addRule(RelativeLayout.CENTER_VERTICAL);
        	params.setMargins(15, 0, 0, 0);
        	viewHolder.name_vk.setLayoutParams(params);
        	viewHolder.name_vk.setText(context.getResources().getString(R.string.c_invite_friend));
	    } 
        if(position > 0){
        	viewHolder.separator.setVisibility(View.GONE);
	        if(letter_buffer.containsKey(position)){        	
	        	viewHolder.letter.setVisibility(View.VISIBLE);
				viewHolder.separator.setVisibility(View.VISIBLE);
				viewHolder.image_separator.setVisibility(View.VISIBLE);	
				viewHolder.letter.setText(letter_buffer.get(position));
	        }
	    	viewHolder.image_ava.setVisibility(View.VISIBLE);
        	viewHolder.name_phone.setVisibility(View.VISIBLE);
        	params.addRule(RelativeLayout.RIGHT_OF, R.id.ivAvatarFriend);
        	params.setMargins(0, 5, 30, 0);
        	viewHolder.name_vk.setLayoutParams(params);
	    	viewHolder.name_vk.setText(values[position].first_name+" "+values[position].last_name);
	    	viewHolder.name_phone.setText(searchPhoneName(values[position].mobile_phone));
        	viewHolder.image_ava.setTag(values[position].photo_rec);
        	loader.download(values[position].photo_rec, viewHolder.image_ava);
	    }
        return convertView;
    }
    
    private String searchPhoneName(String ph){
    	Iterator iterator=list_phone.entrySet().iterator();
    	Log.d("mobile_phone", ph);
    	ph = ph.substring(ph.length()-11, ph.length()-1);
        while(iterator.hasNext()){
            Map.Entry mapEntry=(Map.Entry)iterator.next();
            String ph_cell = (String) mapEntry.getKey();
            ph_cell = ph_cell.substring(ph_cell.length()-11, ph_cell.length()-1);
            if(ph_cell.equals(ph))
            	return (String) mapEntry.getValue();
        }
    	return "";
    }
}