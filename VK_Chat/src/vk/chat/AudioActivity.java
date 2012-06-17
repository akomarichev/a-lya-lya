package vk.chat;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.view.MotionEvent;

public class AudioActivity extends Activity{
	private ImageView playButton;
	private MediaPlayer mediaPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.audio);

	initView();
	initMediaPlayer();
	}

	private void initView() {


	playButton = (ImageView) findViewById(R.id.audio_play);
	playButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			playStream();
		}
	});

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

	private void playStream() {
		try {
			mediaPlayer.setDataSource("http://cs4794.vkontakte.ru//u64330371//audio//10d4f84d1696.mp3");
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopStream() {
		playButton.setVisibility(View.VISIBLE);
		mediaPlayer.stop();
		mediaPlayer.reset();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
			mediaPlayer.release();
		}
	}