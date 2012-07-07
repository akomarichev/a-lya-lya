package vk.chat;

import vk.adapters.ImageDownloader;
import vk.api.API;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserNotRegisteredActivity extends Activity {
	private TextView name;
	private ImageView ava;
	private ImageView back;
	private Button b_to_text;
	private Button b_to_call;
	
	private String url = "";
	private int type = 0;
	private String user_name = "";
	private Long uid = 0L;
	private String phone = "";
	
	private API api;
	private ImageDownloader loader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.synch_user_not_registered);
		
		api = new API(Pref.getAccessTokenHTTPS(UserNotRegisteredActivity.this));
	    loader = new ImageDownloader();
	    
	    checkExtras();
	    setupUI();
	}
	
	private void setupUI(){
		name = (TextView) findViewById(R.id.tv_user);
		name.setText(user_name);
		ava = (ImageView) findViewById(R.id.iv_contacter);
		loader.download(url, ava);
		
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(clickBack);
		
		b_to_text = (Button) findViewById(R.id.b_to_text);
		b_to_text.setOnClickListener(clickToText);
		
		b_to_call = (Button) findViewById(R.id.b_to_call);
		b_to_call.setOnClickListener(clickToCall);
	}
	
	private void checkExtras() {
		Intent i = getIntent();
		Bundle extras = getIntent().getExtras();
		if(i.hasExtra("name")){
			user_name = extras.getString("name");
		}
		if(i.hasExtra("photo")){
			url = extras.getString("photo");
		}
		if(i.hasExtra("uid")){
			uid = extras.getLong("uid");
		}
		if(i.hasExtra("phone")){
			phone = extras.getString("phone");
		}
	}
	
	private OnClickListener clickBack=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private OnClickListener clickToText=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			toText();
		}
	};
	
	private OnClickListener clickToCall=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			toCall();
		}
	};
	
	private void toText(){
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.putExtra("address", phone);
		smsIntent.putExtra("sms_body", getResources().getString(R.string.c_invitation_sms));
		startActivity(smsIntent);		
	}
	
	private void toCall(){
		Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
	}
}
