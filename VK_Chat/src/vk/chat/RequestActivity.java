package vk.chat;

import vk.adapters.ImageDownloader;
import vk.api.API;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RequestActivity extends Activity {
	
	private TextView name;
	private ImageView ava;
	private TextView decline;
	private ImageView back;
	private Button add;
	
	private String url = "";
	private int type = 0;
	private String user_name = "";
	private Long uid = 0L;
	
	private API api;
	private ImageDownloader loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.search_add_user);
	    
	    api = new API(Pref.getAccessTokenHTTPS(RequestActivity.this));
	    loader = new ImageDownloader();
	    
	    checkExtras();
	    setupUI();
	}
	
	private void setupUI(){
		name = (TextView) findViewById(R.id.tv_user);
		name.setText(user_name);
		ava = (ImageView) findViewById(R.id.iv_contacter);
		loader.download(url, ava);
		// load photo
		decline = (TextView) findViewById(R.id.tv_refuse);
		decline.setOnClickListener(clickDecline);
		
		if(type == SearchActivity.SUGGESTION)
			decline.setVisibility(View.GONE);
		
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(clickBack);
		
		add = (Button) findViewById(R.id.b_add);
		add.setOnClickListener(clickAdd);
	}
	
	private void checkExtras() {
		Intent i = getIntent();
		Bundle extras = getIntent().getExtras();
		if(i.hasExtra("type")){
			type = extras.getInt("type");
		}
		if(i.hasExtra("name")){
			user_name = extras.getString("name");
		}
		if(i.hasExtra("photo")){
			url = extras.getString("photo");
		}
		if(i.hasExtra("uid")){
			uid = extras.getLong("uid");
		}
	}
	
	private OnClickListener clickBack=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private OnClickListener clickDecline=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			deleteFriend();
		}
	};
	
	private OnClickListener clickAdd=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			addFriend();
		}
	};
	
	private void addFriend() {
        new Thread(){
            @Override
            public void run(){
            	long response = 0L;
            	try {
					response = api.addFriend(uid);
				} catch (Exception e) {
					e.printStackTrace();
				} 
            	if(response == 2L || response == 1L)
            		runOnUiThread(successAddedFriend);
            	else
            		runOnUiThread(notSuccessAddedFriend);
            }
        }.start();
    }
	
	Runnable successAddedFriend=new Runnable(){
        @Override
        public void run() {
        	// need update db friends
        	Intent intent = new Intent();
			intent.putExtra("need_update", "need_update");
		    setResult(RESULT_OK, intent);
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.sf_success_add_friend), Toast.LENGTH_LONG).show();
        	finish();
        }
    };
    
    Runnable notSuccessAddedFriend=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.sf_fail_add_friend), Toast.LENGTH_LONG).show();
        	finish();
        }
    };
    
    
    private void deleteFriend() {
        new Thread(){
            @Override
            public void run(){
            	long response = 0L;
            	try {
					response = api.deleteFriend(uid);
				} catch (Exception e) {
					e.printStackTrace();
				} 
            	if(response == 1L || response == 2L)
            		runOnUiThread(successDeletedFriend);
            	else
            		runOnUiThread(notSuccessDeletedFriend);
            }
        }.start();
    }
	
	Runnable successDeletedFriend=new Runnable(){
        @Override
        public void run() {
        	Intent intent = new Intent();
			intent.putExtra("need_update", "need_update");
		    setResult(RESULT_OK, intent);
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.sf_success_decline), Toast.LENGTH_LONG).show();
        	finish();
        }
    };
    
    Runnable notSuccessDeletedFriend=new Runnable(){
        @Override
        public void run() {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.sf_fail_decline), Toast.LENGTH_LONG).show();
        	finish();
        }
    };

}
