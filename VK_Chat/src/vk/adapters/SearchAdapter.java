package vk.adapters;

import java.util.HashMap;

import vk.api.User;
import vk.chat.R;
import vk.chat.R.drawable;
import vk.chat.R.id;
import vk.chat.R.layout;
import vk.chat.SearchActivity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchAdapter extends ArrayAdapter<User> {	
    private final Context context;
    private final User[] values;
    //private static TextView text;
    private static TextView image;
    private static LayoutInflater inflater;
    private ImageDownloader loader;
    
    static class ViewHolder {
		public TextView text;
		public ImageView image_ava;
		public ImageView image_online;
		
		public TextView info;
		public RelativeLayout separator;
		public ImageView image_separator;
	}

    public SearchAdapter(Context context, User[] objects) {
        super(context, R.layout.search_list_separator, objects);
        this.context = context;
        this.values = objects;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //loader =  new ImageDownloader();
        loader =  new ImageDownloader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.search_list_separator, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.tvNameFriend);
			viewHolder.image_ava = (ImageView) convertView.findViewById(R.id.ivAvatarFriend);
			viewHolder.image_online = (ImageView) convertView.findViewById(R.id.iv_online);
			
			viewHolder.separator = (RelativeLayout) convertView.findViewById(R.id.layout_separator);
			viewHolder.image_separator = (ImageView) convertView.findViewById(R.id.image_separator);
			viewHolder.info = (TextView) convertView.findViewById(R.id.letter);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        
        viewHolder.separator.setVisibility(View.GONE);
        if(position == 0 && SearchActivity.requests && !SearchActivity.inSearching){        	
        	viewHolder.info.setVisibility(View.VISIBLE);
			viewHolder.separator.setVisibility(View.VISIBLE);
			viewHolder.image_separator.setVisibility(View.VISIBLE);
			viewHolder.info.setText(context.getResources().getString(R.string.sf_friend_requests));
	    }
        else if(position == 0 && !SearchActivity.requests && !SearchActivity.inSearching){
        	viewHolder.info.setVisibility(View.VISIBLE);
			viewHolder.separator.setVisibility(View.VISIBLE);
			viewHolder.image_separator.setVisibility(View.VISIBLE);
			viewHolder.info.setText(context.getResources().getString(R.string.sf_possible_friends));
        }
        if(SearchActivity.separator != 0 && SearchActivity.separator == position && !SearchActivity.inSearching){        	
        	viewHolder.info.setVisibility(View.VISIBLE);
			viewHolder.separator.setVisibility(View.VISIBLE);
			viewHolder.image_separator.setVisibility(View.VISIBLE);
			viewHolder.info.setText(context.getResources().getString(R.string.sf_possible_friends));
	    }
        
        viewHolder.text.setText(values[position].first_name+" "+values[position].last_name);

        if(values[position].online == true){
        	viewHolder.image_online.setVisibility(View.VISIBLE);
        }
        else{
        	viewHolder.image_online.setVisibility(View.INVISIBLE);
        }
        viewHolder.image_ava.setTag(values[position].photo_rec);
        loader.download(values[position].photo_rec, viewHolder.image_ava);
        return convertView;
    }
    
    
}