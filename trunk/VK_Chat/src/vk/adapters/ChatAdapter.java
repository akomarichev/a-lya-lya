package vk.adapters;

import vk.adapters.MySimpleArrayAdapterFast.ViewHolder;
import vk.api.User;
import vk.chat.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final User[] values;
    public static TextView text;
    public static TextView image;
    public static LayoutInflater inflater;
    public ImageDownloaderFast loader;
    
    static class ViewHolder {
		public TextView text;
		public ImageView image_ava;
		public ImageView image_online;
		public ImageView image_delete;
	}

    public ChatAdapter(Context context, User[] objects) {
        super(context, R.layout.chat_users_list, objects);
        this.context = context;
        this.values = objects;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //loader =  new ImageDownloader();
        loader =  new ImageDownloaderFast();
        
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.chat_users_list, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.chat_tv_name);
			viewHolder.image_ava = (ImageView) convertView.findViewById(R.id.chat_iv_ava);
			viewHolder.image_online = (ImageView) convertView.findViewById(R.id.chat_iv_online);
			viewHolder.image_delete = (ImageView) convertView.findViewById(R.id.chat_iv_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        
        viewHolder.text.setText(values[position].first_name+" "+values[position].last_name);
        viewHolder.image_ava.setBackgroundResource(R.drawable.contact_nophoto);
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
