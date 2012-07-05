package vk.adapters;

import vk.adapters.MySimpleArrayAdapterFast.ViewHolder;
import vk.api.API;
import vk.api.User;
import vk.chat.R;
import vk.pref.Pref;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatListAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final User[] values;
    public static TextView text;
    public static TextView image;
    public static LayoutInflater inflater;
    public ImageDownloader loader;
    public API api;
    public Long chat_id;
    
    static class ViewHolder {
		public TextView text;
		public ImageView image_ava;
		public ImageView image_online;
		public ImageView image_delete;
	}

    public ChatListAdapter(Context context, User[] objects, Long chat_id) {
        super(context, R.layout.chat_users_list, objects);
        this.chat_id = chat_id;
        this.context = context;
        this.values = objects;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        api = new API(Pref.getAccessTokenHTTPS(context));
        loader =  new ImageDownloader();
        
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
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
        
        viewHolder.image_delete.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				new AsyncTask<Context, Void, Void>() {

			        @Override
			        protected Void doInBackground(Context... params) {
			        	try {
	    					String response = api.removeChatUser(chat_id, values[position].uid);
	    					Log.d("removeChatUser", response);
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }	
			                return null;
			            } 
			        
			            @Override
			            public void onPostExecute(Void result){
			                notifyDataSetChanged();
			            }
			       }.execute();
			}
		});
        
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
