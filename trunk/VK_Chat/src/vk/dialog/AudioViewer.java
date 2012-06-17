package vk.dialog;

import java.util.ArrayList;

import vk.api.Attachment;
import vk.api.Audio;
import vk.chat.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioViewer implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {
	/* Audio */
	private Audio audio;
	private ImageView play_image;
	private MediaPlayer mediaPlayer;
	private SeekBar seekBar;
	private TextView tv_artist;
	private TextView tv_song;
	
	private final Handler handler = new Handler();
	private int mediaFileLengthInMilliseconds;
	
	public View getAudio(ViewGroup parent, Attachment att, LayoutInflater inflater){
    	View rowViewAudio = inflater.inflate(R.layout.audio, parent, false);
    	audio = new Audio();
    	audio = att.audio;
    	//initViewAudio(rowViewAudio, att);
    	//initMediaPlayer();	
    	initView(rowViewAudio);
		return rowViewAudio;	    	
    }
	
	private void initView(View rowViewAudio) {
		play_image = (ImageView) rowViewAudio.findViewById(R.id.audio_play);
		play_image.setOnClickListener(this);
		
		seekBar = (SeekBar)rowViewAudio.findViewById(R.id.seekBar);	
		seekBar.setMax(99); // It means 100% .0-99
		seekBar.setOnTouchListener(this);
		
		tv_artist = (TextView) rowViewAudio.findViewById(R.id.audio_artist);
		tv_song = (TextView) rowViewAudio.findViewById(R.id.audio_song);		
		
		tv_artist.setText(audio.artist);
		tv_song.setText(audio.title);
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
	}

	/** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
    	seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	primarySeekBarProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}
    }

	@Override
	public void onClick(View v) {
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
			
			primarySeekBarProgressUpdater();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.seekBar){
			/** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
			if(mediaPlayer.isPlaying()){
		    	SeekBar sb = (SeekBar)v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
				mediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		 /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
		play_image.setImageResource(R.drawable.audio_play);
		mediaPlayer.reset();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
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
