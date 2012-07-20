package vk.dialog;

import java.util.ArrayList;

import vk.adapters.DialogAdapter;
import vk.api.Attachment;
import vk.api.Audio;
import vk.chat.R;
import vk.utils.CheckConnection;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioViewer implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {
	/* Audio */
	private Audio audio;
	private ImageView play_image;
	public MediaPlayer mediaPlayer;
	public static SeekBar seekBar;
	public static SeekBar not_static_seekBar; 
	public static Long isPlaying = 0L; 
	public static MediaPlayer playingMediaPlayer;
	
	public static int progress;
	
	private TextView tv_artist;
	private TextView tv_song;
	private ArrayAdapter adapter;
	
	private final Handler handler = new Handler();
	private static int mediaFileLengthInMilliseconds;
	
	public View getAudio(ViewGroup parent, Attachment att, LayoutInflater inflater, ArrayAdapter adapter, Context context){
    	View rowViewAudio = inflater.inflate(R.layout.audio, parent, false);
    	audio = new Audio();
    	audio = att.audio;
    	//initViewAudio(rowViewAudio, att);
    	//initMediaPlayer();	
    	//
    	tv_artist = (TextView) rowViewAudio.findViewById(R.id.audio_artist);
		tv_song = (TextView) rowViewAudio.findViewById(R.id.audio_song);		
		
		tv_artist.setText(audio.artist);
		tv_song.setText(audio.title);
    	this.adapter = adapter;
    	if(CheckConnection.isOnline(context))
    		initView(rowViewAudio);
    	
    	//}
		return rowViewAudio;	    	
    }
	
	private void initView(View rowViewAudio) {
		play_image = (ImageView) rowViewAudio.findViewById(R.id.audio_play);
		play_image.setOnClickListener(this);
		if(isPlaying == audio.aid){
			Log.d("isPlaying","pressed");
			if(playingMediaPlayer.isPlaying()){
				play_image.setImageResource(R.drawable.audio_pause);
			}else {
				play_image.setImageResource(R.drawable.audio_play);
			}
		}
		else 
			play_image.setImageResource(R.drawable.audio_play);
		
		not_static_seekBar = (SeekBar)rowViewAudio.findViewById(R.id.seekBar);
		//seekBar.setMax(99); // It means 100% .0-99
		//seekBar.setOnTouchListener(this);
		//static_seekBar = seekBar;
		not_static_seekBar.setMax(99);
		not_static_seekBar.setOnTouchListener(this);
		if(isPlaying == audio.aid || isPlaying == 0L){
			seekBar = not_static_seekBar;
		}
		
		
		
		
		initMediaFirst();
		
		//adapter.notifyDataSetChanged();
		
	}
	
	private void initMediaFirst(){
		if(isPlaying == 0L || isPlaying != audio.aid){
			if(playingMediaPlayer != null)
				mediaPlayer = playingMediaPlayer;
			else{
	    		mediaPlayer = new MediaPlayer();
	    		playingMediaPlayer = mediaPlayer;
	    		playingMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    		playingMediaPlayer.setOnBufferingUpdateListener(this);
	    		playingMediaPlayer.setOnCompletionListener(this);
	    		
	    		//static_seekBar = seekBar;
			}
		}
    	else {
			mediaPlayer = playingMediaPlayer;
			//seekBar = static_seekBar;
			//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    		//mediaPlayer.setOnBufferingUpdateListener(this);
    		//mediaPlayer.setOnCompletionListener(this);
    	}
		
	}
	
	private void initMedia(){
		if(isPlaying == 0L || audio.aid != isPlaying){
//    		//if(playingMediaPlayer != null)
//    			playingMediaPlayer.reset();
//
//    		mediaPlayer = new MediaPlayer();
//    		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//    		mediaPlayer.setOnBufferingUpdateListener(this);
//    		mediaPlayer.setOnCompletionListener(this);
//    		playingMediaPlayer = mediaPlayer;
			if(playingMediaPlayer != null){
				playingMediaPlayer.reset();
			}
	    		mediaPlayer = new MediaPlayer();
	    		playingMediaPlayer = mediaPlayer;
	    		playingMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    		playingMediaPlayer.setOnBufferingUpdateListener(this);
	    		playingMediaPlayer.setOnCompletionListener(this);
	    		
	    		//static_seekBar = seekBar;
		}
		else{
			mediaPlayer = playingMediaPlayer;
			//seekBar = static_seekBar;
		}
		//seekBar = static_seekBar;	
    		//seekBar.setMax(99); // It means 100% .0-99
    		//seekBar.setOnTouchListener(this);
    			
	}

	/** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
    	//initMedia();
    	//if(audio.aid == isPlaying)
			//mediaPlayer = playingMediaPlayer;
    	if(audio.aid == isPlaying){
    		mediaPlayer = playingMediaPlayer;
    		//seekBar = static_seekBar;
    	progress = (int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100);
    	seekBar.setProgress(progress); // This math construction give a percentage of "was playing"/"song length"
		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	primarySeekBarProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,300);
    	}
    	}
    }

	@Override
	public void onClick(View v) {
		//Log.d("audioclick","pressed");
		//static_seekBar=seekBar;
		if(isPlaying == 0L || audio.aid != isPlaying){
			initMedia();
			isPlaying = audio.aid;			
		}
		//adapter.notifyDataSetChanged();
		if(v.getId() == R.id.audio_play){
			 /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */			
			try {
				mediaPlayer.setDataSource(audio.url); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
				mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer. 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
			
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				play_image.setImageResource(R.drawable.audio_pause);
			}else {
				mediaPlayer.pause();
				play_image.setImageResource(R.drawable.audio_play);
			}
			adapter.notifyDataSetChanged();
			//playingMediaPlayer = mediaPlayer;
			primarySeekBarProgressUpdater();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//Log.d("audiotouch","pressed");
		//initMedia();
		if(audio.aid == isPlaying){
			//seekBar = static_seekBar;
    		mediaPlayer = playingMediaPlayer;
			if(v.getId() == R.id.seekBar){
				/** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
				if(mediaPlayer.isPlaying()){
			    	SeekBar sb = (SeekBar)v;
					int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
					mediaPlayer.seekTo(playPositionInMillisecconds);
				}
			}
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		//Log.d("audiocom","pressed");
		 /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
		//initMedia();
		play_image.setImageResource(R.drawable.audio_play);
    	if(playingMediaPlayer != null)
    		playingMediaPlayer.reset();
		//mediaPlayer.reset();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		//Log.d("audiobuf","pressed");
		/** Method which updates the SeekBar secondary progress by current song loading from URL position*/
		seekBar.setSecondaryProgress(percent);
	}
    
    /*public void initViewAudio(View rowViewAudio, Attachment att){
    	audio = new Audio();
		audio = att.audio;
    	
    	play_image = (ImageView) rowViewAudio.findViewById(R.id.audio_play);
    	play_image.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			playStream(audio.url);
    		}
    	});
    	
		seekBar = (SeekBar) rowViewAudio.findViewById(R.id.seekBar);
		
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

	        @Override
	        public void onStopTrackingTouch(SeekBar seekBar1) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onStartTrackingTouch(SeekBar seekBar1) {
	            // TODO Auto-generated method stub
	        }

	        @Override
	        public void onProgressChanged(SeekBar seekBar1, int progress,
	                boolean fromUser) {
	            // TODO Auto-generated method stub

	            if(fromUser){
	                mediaPlayer.seekTo(progress);
	                seekBar.setProgress(progress);
	            }

	        }
	    });
		
		tv_artist = (TextView) rowViewAudio.findViewById(R.id.audio_artist);
		tv_song = (TextView) rowViewAudio.findViewById(R.id.audio_song);		
		
		tv_artist.setText(audio.artist);
		tv_song.setText(audio.title);
    }
    
    private void playStream(String url) {
		try {
			seekBar.setMax(99);
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		primarySeekBarProgressUpdater();
	}
    
    private void initMediaPlayer() {
    	mediaPlayer = new MediaPlayer();
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    		@Override
    		public void onPrepared(MediaPlayer mp) {
    			mediaPlayer.start();
    		}
    	});
    }
    
    private void primarySeekBarProgressUpdater() {
    	seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/audio.duration)*100)); // This math construction give a percentage of "was playing"/"song length"
		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	primarySeekBarProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1);
    	}
		
    }*/
}
