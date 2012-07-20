package vk.dialog;

import java.util.ArrayList;

import vk.adapters.ImageDownloader;
import vk.api.Attachment;
import vk.api.Audio;
import vk.api.Photo;
import vk.api.Video;
import vk.chat.HandsetContactsActivity;
import vk.chat.PhotoViewerActivity;
import vk.chat.R;
import vk.chat.UserRegisteredActivity;
import vk.utils.CheckConnection;
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
import android.widget.SeekBar;
import android.widget.TextView;

public class PhotoViewer{
	/* Audio */
	private Photo photo;
	private ImageView photo_image;
	private ImageDownloader loader ;
	private TextView name;
	private Context context;
	
	public View getPhoto(ViewGroup parent, Attachment att, LayoutInflater inflater, Context context){
    	View rowViewPhoto = inflater.inflate(R.layout.photo_viewer, parent, false);
    	photo = new Photo();
    	photo = att.photo;
    	this.context=context;
    	loader = new ImageDownloader();
    	initView(rowViewPhoto);
		return rowViewPhoto;	    	
    }
	
	private void initView(View rowViewPhoto) {
		photo_image = (ImageView) rowViewPhoto.findViewById(R.id.iv_photo);
		name = (TextView) rowViewPhoto.findViewById(R.id.tv_photo_name);
		name.setText(photo.phototext);
		Log.d("photo", photo.src);
		/*Log.d("photo_big", photo.src_big);
		Log.d("photo_small", photo.src_small);
		Log.d("photo_src_xbig", photo.src_xbig);
		Log.d("photo_xxbig", photo.src_xxbig);*/
		if(CheckConnection.isOnline(context))
			loader.download(photo.src, photo_image);
		photo_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PhotoViewerActivity.class);
					intent.putExtra("photo_name", photo.phototext);
					intent.putExtra("photo", photo.src);
					context.startActivity(intent);			
				}
			});
		//photo_image.setOnClickListener(this);
	}
}
