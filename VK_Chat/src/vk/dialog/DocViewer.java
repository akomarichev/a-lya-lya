package vk.dialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import vk.adapters.ImageDownloader;
import vk.api.API;
import vk.api.Attachment;
import vk.api.Audio;
import vk.api.Doc;
import vk.api.Photo;
import vk.api.Video;
import vk.chat.R;
import vk.pref.Pref;
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
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class DocViewer{
	/* Audio */
	private Doc doc;
	VideoView viewer;
	Context context;
	API api;
	
	private TextView name;
	private LinearLayout doc_l;
	
	public View getDoc(ViewGroup parent, Attachment att, LayoutInflater inflater, Context context){
		api = new API(Pref.getAccessTokenHTTPS(context));
    	View rowViewDoc = inflater.inflate(R.layout.doc_view, parent, false);
    	doc = new Doc();
    	doc = att.doc;
    	this.context = context;
    	initView(rowViewDoc);
    	
		return rowViewDoc;	    	
    }
	
	private void initView(View rowViewDoc) {
		name = (TextView) rowViewDoc.findViewById(R.id.doc);
		name.setText(doc.title+"."+doc.ext);
		if(CheckConnection.isOnline(context)){
			doc_l = (LinearLayout) rowViewDoc.findViewById(R.id.doc_l);
			doc_l.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<Doc> list = null;
					try{
						list = api.getDoc(doc.did, doc.owner_id);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(list!=null)
						context.startActivity(new Intent (Intent.ACTION_VIEW,Uri.parse(list.get(0).url)));				
				}
			});
		}
	}
}
