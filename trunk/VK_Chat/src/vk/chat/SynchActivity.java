package vk.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SynchActivity extends Activity {

	Button launchButton;
	/** Called when the activity is first created. */ @Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.synch);
		launchButton = (Button)findViewById(R.id.synch);
		launchButton.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v) {               
		} }); 
	}
}