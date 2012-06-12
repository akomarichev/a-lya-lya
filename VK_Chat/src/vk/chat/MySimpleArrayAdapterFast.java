package vk.chat;

import vk.api.ImageDownloader;
import vk.api.User;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapterFast extends ArrayAdapter<User> {	
    private final Context context;
    private final User[] values;
    public static TextView text;
    public static TextView image;
    public static LayoutInflater inflater;
    public ImageDownloader loader;
    
    static class ViewHolder {
		public TextView text;
		public ImageView image_ava;
		public ImageView image_online;
	}

    public MySimpleArrayAdapterFast(Context context, User[] objects) {
        super(context, R.layout.friends_list, objects);
        this.context = context;
        this.values = objects;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loader =  new ImageDownloader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.friends_list, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.tvNameFriend);
			viewHolder.image_ava = (ImageView) convertView.findViewById(R.id.ivAvatarFriend);
			viewHolder.image_online = (ImageView) convertView.findViewById(R.id.iv_online);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        
        viewHolder.text.setText(values[position].first_name+" "+values[position].last_name);
        viewHolder.image_ava.setBackgroundResource(R.drawable.empty_photo);
        Log.d("Online",values[position].online.toString());
        if(values[position].online == true){
        	viewHolder.image_online.setBackgroundResource(R.drawable.online_list);
        }
        else{
        	viewHolder.image_online.setBackgroundColor(Color.BLACK);
        }
        viewHolder.image_ava.setTag(values[position].photo_rec);
        loader.download(values[position].photo_rec, viewHolder.image_ava);
        return convertView;
    }
    
    
}