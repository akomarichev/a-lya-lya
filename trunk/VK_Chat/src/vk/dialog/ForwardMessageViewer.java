package vk.dialog;

import java.util.ArrayList;

import vk.adapters.ImageDownloader;
import vk.api.API;
import vk.api.Attachment;
import vk.api.Audio;
import vk.api.ForwardMessages;
import vk.api.Message;
import vk.api.Photo;
import vk.api.User;
import vk.chat.R;
import vk.pref.Pref;
import vk.utils.WorkWithTimeAndDate;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ForwardMessageViewer{

	private ImageView ava;
	private TextView name;
	private TextView time;
	private TextView message;
	private ImageDownloader loader ;
	private ForwardMessages m;
	private Context c;
	private API api;
	private ArrayList<User> user = new ArrayList<User>();
	
	public View getForwardMessage(ViewGroup parent, LayoutInflater inflater, ForwardMessages m, Context context){
    	View rowViewPhoto = inflater.inflate(R.layout.forward_message, parent, false);
    	loader = new ImageDownloader();
    	this.m = m;
    	c = context;
    	api = new API(Pref.getAccessTokenHTTPS(context));
    	initView(rowViewPhoto);
		return rowViewPhoto;	    	
    }
	
	private void initView(View rowViewPhoto) {
		ava = (ImageView) rowViewPhoto.findViewById(R.id.iv_ava);
		name = (TextView) rowViewPhoto.findViewById(R.id.tv_name);
		time = (TextView) rowViewPhoto.findViewById(R.id.tv_time);
		message = (TextView) rowViewPhoto.findViewById(R.id.tv_message);
		time.setText(WorkWithTimeAndDate.getTime(m.date, c));
		message.setText(m.body);
		
		new AsyncTask<Context, Void, Void>() {

	        @Override
	        protected Void doInBackground(Context... params) {
	        	try {
					user = api.getUsers(Long.toString(m.uid));
                } catch (Exception e) {
                    e.printStackTrace();
                }	
	                return null;
	            } 
	        
	            @Override
	            public void onPostExecute(Void result){
	            	name.setText(user.get(0).first_name + " " + user.get(0).last_name);	                
			        loader.download(user.get(0).photo_rec, ava);
	            }
	       }.execute();
	}
}
