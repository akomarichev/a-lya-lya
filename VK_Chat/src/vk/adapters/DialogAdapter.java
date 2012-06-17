package vk.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vk.api.Attachment;
import vk.api.Audio;
import vk.api.Message;
import vk.chat.R;
import vk.dialog.AudioViewer;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

public class DialogAdapter extends ArrayAdapter<Message> {

	
	
		private ArrayList<Integer> checkedItems;
		private static final String TAG = "DialogAdapter";
	    private final Context context;
	    private final Message[] values;
	    public static TextView text;
	    //public static TextView image;
	    public static LayoutInflater inflater;
	    public static LayoutInflater audio_inflater;
	    private LinearLayout wrapper;
	    public static int last;
	    public static Button b;
	    //public ImageDownloader loader;
	    
	    class ViewHolder {
			public TextView text;
			public LinearLayout wrapper;
			//public ImageView image_ava;
			//public ImageView image_online;
		}

	    public DialogAdapter(Context context, Message[] objects, ArrayList<Integer> list) {
	        super(context, R.layout.listitem_dialog, objects);
	        this.context = context;
	        this.values = objects;
	        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        this.audio_inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        this.checkedItems = list;
	        //loader =  new ImageDownloader();
	    }

	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	final ViewHolder viewHolder = new ViewHolder();
	        convertView = inflater.inflate(R.layout.listitem_dialog, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.iv_itemlist_dialog);
			viewHolder.wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper2);
			
			
				
	        viewHolder.text.setText(values[position].body);
	        LayoutParams lpView = (LayoutParams) viewHolder.wrapper.getLayoutParams();
	        lpView.gravity = values[position].is_out ? Gravity.RIGHT : Gravity.LEFT;
	        lpView.height = LayoutParams.WRAP_CONTENT;
	        lpView.width = LayoutParams.WRAP_CONTENT;
	        if(values[position].is_out){
	        	lpView.leftMargin = 10;
	        }else{
	        	lpView.rightMargin = 10;
	        }
	        viewHolder.wrapper.setLayoutParams(lpView);
	        viewHolder.wrapper.setBackgroundResource(values[position].is_out ? R.drawable.dialog_msgout : R.drawable.dialog_msgin_inbox);
	        
	        if(checkedItems.contains((Integer) position)){
	        	viewHolder.wrapper.setBackgroundResource(values[position].is_out ? R.drawable.dialog_msgout_selected : R.drawable.dialog_msgin_selected);
	        }
	        
	        
	        viewHolder.wrapper.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					checkedItems.add(position);
					viewHolder.wrapper.setBackgroundResource(values[position].is_out ? R.drawable.dialog_msgout_selected : R.drawable.dialog_msgin_selected);
				}
			});
	        
	        //b.setText("Hello!");
	        
	        
	        if(values[position].attachments != null){
	        	Log.d(TAG, "attachments");
				for(Attachment att:values[position].attachments){
					if(att.type.equals("photo")){
						
					}
					
					if(att.type.equals("audio")){
						viewHolder.wrapper.addView(new AudioViewer().getAudio(parent, att, inflater));	
					}
					
					if(att.type.equals("video")){						
						
					}
				}			
			}	        
	        return convertView;
	    }  
	    
	}