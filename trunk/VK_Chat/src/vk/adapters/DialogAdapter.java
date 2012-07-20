package vk.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vk.api.Attachment;
import vk.api.Audio;
import vk.api.ForwardMessages;
import vk.api.Message;
import vk.chat.DialogActivity;
import vk.chat.R;
import vk.dialog.AudioViewer;
import vk.dialog.ForwardMessageViewer;
import vk.dialog.PhotoViewer;
import vk.utils.WorkWithTimeAndDate;
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
	    
	    static int k = 0;
	    //public ImageDownloader loader;
	    
	    class ViewHolder {
			public TextView text;
			public TextView time_left;
			public TextView time_right;
			public LinearLayout layout;
			public LinearLayout wrapper;
			public LinearLayout wrapper_right;
			public ImageView status;
			//public ImageView image_ava;
			//public ImageView image_online;
		}
	    
	    static class ViewHolderAudio{
	    	public View audio;		
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
			viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.wrapper);
			viewHolder.wrapper_right = (LinearLayout) convertView.findViewById(R.id.wrapper3);
			viewHolder.time_left = (TextView) convertView.findViewById(R.id.iv_itemlist_time_left);
			viewHolder.time_right = (TextView) convertView.findViewById(R.id.iv_itemlist_time_right);
			viewHolder.status = (ImageView) convertView.findViewById(R.id.iv_itemlist_status);
			
			viewHolder.status.setVisibility(View.GONE);
			
	        viewHolder.text.setText(values[position].body);
	        LayoutParams lpView = (LayoutParams) viewHolder.wrapper_right.getLayoutParams();
	        lpView.gravity = values[position].is_out ? Gravity.RIGHT : Gravity.LEFT;
	        lpView.height = LayoutParams.WRAP_CONTENT;
	        lpView.width = LayoutParams.WRAP_CONTENT;
	        
	        /*LayoutParams lpView3 = (LayoutParams) viewHolder.wrapper_right.getLayoutParams();
	        lpView.gravity = Gravity.RIGHT;
	        lpView.height = LayoutParams.WRAP_CONTENT;
	        lpView.width = LayoutParams.WRAP_CONTENT;*/
	        
	        if(values[position].is_out){
	        	viewHolder.time_left.setVisibility(View.VISIBLE);
	        	viewHolder.time_left.setText(WorkWithTimeAndDate.getTime(values[position].date, context));	        	
	        	//viewHolder.time_left.set
	        	viewHolder.time_right.setVisibility(View.GONE);
	        	if(position == DialogActivity.maxposition && DialogActivity.isDelivered){
		        	viewHolder.status.setVisibility(View.VISIBLE);
		        }
	        	//lpView.leftMargin = 30;
	        	//lpView.rightMargin = 0;
	        }else{
	        	viewHolder.time_left.setVisibility(View.GONE);
	        	viewHolder.time_right.setVisibility(View.VISIBLE);
	        	//viewHolder.status.setVisibility(View.GONE);
	        	viewHolder.time_right.setText(WorkWithTimeAndDate.getTime(values[position].date, context));
	        	//lpView.rightMargin = 30;
	        	//lpView.leftMargin = 0;
	        }
	        //viewHolder.wrapper_right.setLayoutParams(lpView3);
	        viewHolder.wrapper_right.setLayoutParams(lpView);
	        if(values[position].read_state.equals("0"))
	        	viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.unread_message));
	        //
	        viewHolder.wrapper.setBackgroundResource(values[position].is_out ? R.drawable.dialog_msgout : R.drawable.dialog_msgin_inbox);
	        if(checkedItems.contains((Integer) position)){	        	
	        	viewHolder.wrapper.setBackgroundResource(values[position].is_out ? R.drawable.dialog_msgout_selected : R.drawable.dialog_msgin_selected);
	        	viewHolder.text.setTextColor(context.getResources().getColor(R.color.white_color));
	        }
	        
	        
	        /*viewHolder.wrapper.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Log.d("audio","pressed");
					//checkedItems.add(position);
					//viewHolder.wrapper.setBackgroundResource(values[position].is_out ? R.drawable.dialog_msgout_selected : R.drawable.dialog_msgin_selected);
				}
			});*/
	        
	        //b.setText("Hello!");
	        
	        
	        if(values[position].attachments != null){
	        	Log.d(TAG, "attachments");
				for(Attachment att:values[position].attachments){
					if(att.type.equals("photo")){
						viewHolder.wrapper.addView(new PhotoViewer().getPhoto(parent, att, inflater, context));
						
					}
					
					if(att.type.equals("audio")){
						//ViewHolderAudio viewHolderAudio = new ViewHolderAudio();
//						if(k==0){
//							viewHolderAudio.audio = new AudioViewer().getAudio(parent, att, inflater);
//							convertView.setTag(viewHolderAudio);
//							k = position;
//							Log.d("audio","if");
//						}
//						else{
//							viewHolderAudio = (ViewHolderAudio) convertView.getTag();
//							Log.d("audio","else");
//						}
//							//audio = (View) convertView.findViewWithTag(position);		
//						if(viewHolderAudio != null)
//							viewHolder.wrapper.addView(viewHolderAudio.audio);
						viewHolder.wrapper.addView(new AudioViewer().getAudio(parent, att, inflater, this, context));
					}
					
					if(att.type.equals("video")){						
						
					}
				}			
			}	        
	        
	        if(values[position].f_msgs != null){
	        	Log.d(TAG, "attachments");
				for(ForwardMessages att:values[position].f_msgs)
					viewHolder.wrapper.addView(new ForwardMessageViewer().getForwardMessage(parent, inflater, att, context));		
			}	    
	        
	        
	        	
	        
	        return convertView;
	    }  
	    
	}