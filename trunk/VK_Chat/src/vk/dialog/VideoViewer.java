package vk.dialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import vk.adapters.ImageDownloader;
import vk.api.API;
import vk.api.Attachment;
import vk.api.Audio;
import vk.api.Photo;
import vk.api.Video;
import vk.chat.R;
import vk.pref.Pref;
import vk.utils.CheckConnection;
import vk.utils.WorkWithTimeAndDate;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoViewer{
	/* Audio */
	private Video video;
	private ImageView photo_image;
	private ImageDownloader loader ;
	VideoView viewer;
	Context context;
	API api;
	
	private TextView duration;
	private ImageView back;
	private ImageView play;
	private TextView name;
	
	public View getVideo(ViewGroup parent, Attachment att, LayoutInflater inflater, Context context){
		api = new API(Pref.getAccessTokenHTTPS(context));
    	View rowViewVideo = inflater.inflate(R.layout.video_view, parent, false);
    	video = new Video();
    	video = att.video;
    	loader = new ImageDownloader();
    	this.context = context;
    	initView(rowViewVideo);
    	
		return rowViewVideo;	    	
    }
	
	private void initView(View rowViewVideo) {
		play = (ImageView) rowViewVideo.findViewById(R.id.iv_play);	
		back = (ImageView) rowViewVideo.findViewById(R.id.iv_back);
		duration = (TextView) rowViewVideo.findViewById(R.id.tv_dur);
		name = (TextView) rowViewVideo.findViewById(R.id.tv_video_name);
		duration.setText(WorkWithTimeAndDate.convertToTime(video.duration+""));
		name.setText(video.description);
		if(CheckConnection.isOnline(context)){
			loader.download(video.image, back);
				
			play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<Video> list = null;
					try{
						list = api.getVideo(video.vid, video.owner_id);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(list!=null)
						context.startActivity(new Intent (Intent.ACTION_VIEW,Uri.parse(list.get(0).player)));				
				}
			});
		}
		
	}
}
